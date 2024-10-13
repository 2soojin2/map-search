package com.example.mapsearch.service;

import com.example.mapsearch.entity.KeywordEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class KeyWordRedisServiceTest {
    @Autowired
    private KeywordRedisService keyWordRedisService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @BeforeEach
    public void setup() {
        // Redis 데이터 비우기
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void 키워드_저장(){
        KeywordEntity keyword = new KeywordEntity("곱창");
        KeywordEntity keywordEntity = keyWordRedisService.saveKeyword(keyword);
        Assertions.assertEquals(keywordEntity.getId(), keywordEntity.getId());
    }

    @Test
    void 이미_존재하는_키워드_저장(){
        String q = "곱창";
        KeywordEntity keyword = new KeywordEntity(q);
        keyWordRedisService.saveOrUpdateKeyword(keyword);
        KeywordEntity byTitle = keyWordRedisService.findByTitle(keyword);
        Assertions.assertEquals(0, byTitle.getSearchCount());
        Double placeSearchCount = keyWordRedisService.getKeyWordSearchCount(byTitle.getId());
        Assertions.assertEquals(0, placeSearchCount);

        // 한번 더 저장
        keyWordRedisService.saveOrUpdateKeyword(keyword);
        KeywordEntity byTitle2 = keyWordRedisService.findByTitle(keyword);
        Assertions.assertEquals(1, byTitle.getSearchCount());
        Double placeSearchCount2 = keyWordRedisService.getKeyWordSearchCount(byTitle2.getId());
        Assertions.assertEquals(1, placeSearchCount2);
    }

    @Test
    void 키워드로_객체_찾기(){
        String q = "곱창";
        KeywordEntity keyword = new KeywordEntity(q);
        KeywordEntity keywordEntity = keyWordRedisService.saveOrUpdateKeyword(keyword);
        KeywordEntity byTitle = keyWordRedisService.findByTitle(keyword);
        Assertions.assertEquals(keywordEntity.getId(), byTitle.getId());
    }

}