package com.example.mapsearch.service;

import com.example.mapsearch.entity.KeywordEntity;
import com.example.mapsearch.facade.RedissonLockKeywordFacade;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@SpringBootTest
class KeyWordRedisServiceTest {
    @Autowired
    private KeywordRedisService keyWordRedisService;
    @Autowired
    private StringRedisTemplate redisTemplate;
    @Autowired
    private RedissonLockKeywordFacade redissonLockKeywordFacade;
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
        Assertions.assertEquals(1, byTitle.getSearchCount());
        Double placeSearchCount = keyWordRedisService.getKeyWordSearchCount(byTitle.getId());
        Assertions.assertEquals(1, placeSearchCount);

        // 한번 더 저장
        keyWordRedisService.saveOrUpdateKeyword(keyword);
        KeywordEntity byTitle2 = keyWordRedisService.findByTitle(keyword);
        Assertions.assertEquals(2, byTitle2.getSearchCount());
        Double placeSearchCount2 = keyWordRedisService.getKeyWordSearchCount(byTitle2.getId());
        Assertions.assertEquals(2, placeSearchCount2);
    }

    @Test
    void 키워드로_객체_찾기(){
        String q = "곱창";
        KeywordEntity keyword = new KeywordEntity(q);
        keyWordRedisService.saveOrUpdateKeyword(keyword);
        KeywordEntity byTitle = keyWordRedisService.findByTitle(keyword);
        Assertions.assertEquals(keyword.getId(), byTitle.getId());
    }

    @Test
    void 동시에_100개의_요청() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(threadCount);

        String q = "곱도리";
        KeywordEntity keyword = new KeywordEntity(q);

        for (int i = 0; i < threadCount ; i++) {
            executorService.submit(() -> {
                try {
                    redissonLockKeywordFacade.saveOrUpdateKeyword(keyword);
//                    keyWordRedisService.saveOrUpdateKeyword(keyword);
                } finally {
                    latch.countDown();
                }
            });
        }
        latch.await();
        KeywordEntity byTitle = keyWordRedisService.findByTitle(keyword);
        Assertions.assertEquals(100, byTitle.getSearchCount());
        Double placeSearchCount = keyWordRedisService.getKeyWordSearchCount(byTitle.getId());
        Assertions.assertEquals(100, placeSearchCount);
    }

}