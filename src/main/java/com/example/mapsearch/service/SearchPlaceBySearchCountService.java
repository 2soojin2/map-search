package com.example.mapsearch.service;

import com.example.mapsearch.entity.PlaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class SearchPlaceBySearchCountService {

    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;
//    private final PlaceRedisRepository placeRedisRepository;
    private final PlaceRedisService placeRedisSerivce;

    private final int START = 0;
    private final int END = 9;
    private final String PLACE_SEARCH_COUNT = "place_search_count";


    @Autowired
    public SearchPlaceBySearchCountService(RedisTemplate<String, Object> redisTemplate, PlaceRedisService placeRedisSerivce) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
        this.placeRedisSerivce = placeRedisSerivce;
    }

    public List<PlaceEntity> getPlaceRanking() {
        List<PlaceEntity> result = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> placeIdAndSearchCount = zSetOperations.reverseRangeWithScores(PLACE_SEARCH_COUNT, START, END);
        for (ZSetOperations.TypedTuple<Object> stringTypedTuple : placeIdAndSearchCount) {
            String placeId = stringTypedTuple.getValue().toString();
            PlaceEntity place = placeRedisSerivce.findById(placeId);
            result.add(place);
        }
        return result;
    }

    public void savePlaceSearchCount(String placeId, double searchCount) {
        zSetOperations.add(PLACE_SEARCH_COUNT, placeId, searchCount);
    }
}
