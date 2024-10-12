package com.example.mapsearch.service;

import com.example.mapsearch.entity.PlaceEntity;
import com.example.mapsearch.repository.PlaceRedisRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.List;

@SpringBootTest
class SearchPlaceBySearchCountServiceTest {

    @Autowired
    private SearchPlaceBySearchCountService service;

    @Autowired
//    private PlaceRedisRepository repository;
    private PlaceRedisService repository;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    public void setup() {
        // Redis 데이터 비우기
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void 검색된_장소가_없을때(){

        List<PlaceEntity> placeSearchCount = service.getPlaceRanking();
        Assertions.assertEquals(0, placeSearchCount.size());
    }

    private void initData(){
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

    private void makePlace(String A곱창, int searchCount) {
        PlaceEntity place1 = new PlaceEntity(A곱창, "127", "165", searchCount);
        PlaceEntity savedPlace = repository.save(place1);
        service.savePlaceSearchCount(savedPlace.getId(), searchCount);
    }

    @Test
    void 검색량_상위_10개의_장소_가져옴() {
        initData();
        List<PlaceEntity> placeSearchCount = service.getPlaceRanking();
        Assertions.assertEquals(10, placeSearchCount.size());
        Assertions.assertEquals(11, placeSearchCount.get(0).getSearchCount());
        Assertions.assertEquals("L곱창", placeSearchCount.get(0).getTitle());
    }
}