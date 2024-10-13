package com.example.mapsearch.service;

import com.example.mapsearch.dto.KeywordRankResDTO;
import com.example.mapsearch.entity.KeywordEntity;
import com.example.mapsearch.entity.PlaceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@SpringBootTest
class SearchPlaceRankingServiceTest {

    @Autowired
    private SearchPlaceRankingService searchPlaceRankingService;

    @Autowired
//    private PlaceRedisRepository repository;
    private PlaceRedisService placeRedisService;

    @Autowired
//    private PlaceRedisRepository repository;
    private KeywordRedisService keywordRedisService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    public void setup() {
        // Redis 데이터 비우기
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void 검색된_장소가_없을때(){

        List<PlaceEntity> placeSearchCount = searchPlaceRankingService.getPlaceRanking();
        Assertions.assertEquals(0, placeSearchCount.size());
    }

    private void initPlaceData(){
        makePlace("A곱창", 0);
        makePlace("B곱창", 1);
        makePlace("C곱창", 2);
        makePlace("D곱창", 3);
        makePlace("E곱창", 4);
        makePlace("F곱창", 5);
        makePlace("G곱창", 6);
        makePlace("H곱창", 7);
        makePlace("I곱창", 8);
        makePlace("J곱창", 9);
        makePlace("K곱창", 10);
        makePlace("L곱창", 11);
    }

    private void makePlace(String title, int searchCount) {
        PlaceEntity place1 = new PlaceEntity(title, "127", "165", searchCount);
        PlaceEntity savedPlace = placeRedisService.savePlace(place1);
        placeRedisService.savePlaceSearchCount(savedPlace.getId(), searchCount);
    }

    @Test
    void 상위_10개의_검색어_가져옴() {
        initKeywordData();
        List<PlaceEntity> placeSearchCount = searchPlaceRankingService.getPlaceRanking();
        Assertions.assertEquals(10, placeSearchCount.size());
        Assertions.assertEquals(11, placeSearchCount.get(0).getSearchCount());
        Assertions.assertEquals("L곱창", placeSearchCount.get(0).getTitle());
    }

    private void initKeywordData(){
        List<String> keywords = Arrays.asList("곱창", "은행", "bank", "bank2", "bank3", "bank4",  "bank5", "bank6", "bank7", "bank8", "bank9");
        for (int i = 0; i < keywords.size(); i++) {
            String kw= keywords.get(i);
            KeywordEntity keywordEntity = new KeywordEntity(kw, i);
            keywordRedisService.saveOrUpdateKeyword(keywordEntity);
        }
    }

    @Test
    void 검색된_키워드가_없을때(){
        List<KeywordRankResDTO> keywordRanking = searchPlaceRankingService.getKeywordRanking();
        Assertions.assertEquals(0, keywordRanking.size());
    }

    @Test
    void 검색량_상위_10개의_장소_가져옴(){
        initKeywordData();
        List<KeywordRankResDTO> keywordRanking = searchPlaceRankingService.getKeywordRanking();
        Assertions.assertEquals(10, keywordRanking.size());
        Assertions.assertEquals("bank9", keywordRanking.get(0).getKeyword());
    }
}