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

    private final RedisTemplate<String, String> redisTemplate;
    private ZSetOperations<String, String> zSetOperations;
//    private final PlaceRedisRepository placeRedisRepository;
    private final PlaceRedisService placeRedisRepository;

    private final int START = 0;
    private final int END = 9;
    private final String PLACE_SEARCH_COUNT = "place_search_count";


    @Autowired
    public SearchPlaceBySearchCountService(RedisTemplate<String, String> redisTemplate, PlaceRedisService placeRedisRepository) {
        this.redisTemplate = redisTemplate;
        this.zSetOperations = redisTemplate.opsForZSet();
        this.placeRedisRepository = placeRedisRepository;
    }

    public List<PlaceEntity> getPlaceRanking() {
        List<PlaceEntity> result = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<String>> placeIdAndSearchCount = zSetOperations.reverseRangeWithScores(PLACE_SEARCH_COUNT, START, END);
        for (ZSetOperations.TypedTuple<String> stringTypedTuple : placeIdAndSearchCount) {
            String placeId = stringTypedTuple.getValue();
            PlaceEntity place = placeRedisRepository.findById(placeId);
            result.add(place);
        }
        return result;
    }

    public void savePlaceSearchCount(String placeId, double searchCount) {
        zSetOperations.add(PLACE_SEARCH_COUNT, placeId, searchCount);
    }
}
