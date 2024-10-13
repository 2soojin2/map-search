package com.example.mapsearch.service;

import com.example.mapsearch.entity.PlaceEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

@SpringBootTest
class PlaceRedisServiceTest {
    @Autowired
    PlaceRedisService placeRedisService;
    @Autowired
    SearchPlaceBySearchCountService searchPlaceBySearchCountService;
    @Autowired
    private StringRedisTemplate redisTemplate;

    @BeforeEach
    public void setup() {
        // Redis 데이터 비우기
        redisTemplate.getConnectionFactory().getConnection().flushAll();
    }

    @Test
    void 저장_혹은_업데이트_값이_있을때(){
        PlaceEntity exPlace = new PlaceEntity("A곱창", "127", "165", 7);

        PlaceEntity place = placeRedisService.saveOrUpdatePlace(exPlace);
        PlaceEntity place2 = placeRedisService.saveOrUpdatePlace(exPlace);

        PlaceEntity byId = placeRedisService.findById(place2.getId());
        Assertions.assertEquals(8, byId.getSearchCount());
        // place_search_count가 업데이트 되는지 확인
        Double placeSearchCount = placeRedisService.getPlaceSearchCount(byId.getId());
        Assertions.assertEquals(8, placeSearchCount);
    }

    @Test
    void 저장_혹은_업데이트_값이_없을때(){
        PlaceEntity exPlace = new PlaceEntity("A곱창", "1234567", "891234", 0);
        PlaceEntity savedPlace = placeRedisService.saveOrUpdatePlace(exPlace);
        Assertions.assertEquals(exPlace.getId(), savedPlace.getId());
    }

    @Test
    void 업데이트시_동시성_이슈_테스트(){
        // 스레드 여러개로 확인
    }

    @Test
    void 좌표값이_같으면_장소_엔티티_반환(){
        PlaceEntity place1 = new PlaceEntity("ABC곱창", "127456", "165123", 0);
        PlaceEntity savePlace1 = placeRedisService.save(place1);
        PlaceEntity place2 = new PlaceEntity("ABCD곱창", "127456", "165123", 1);

        PlaceEntity byCoordinates = placeRedisService.findByCoordinates(place2);
        Assertions.assertEquals(byCoordinates.getId(), savePlace1.getId());

    }
}