package com.example.mapsearch.service;

import com.example.mapsearch.entity.PlaceEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class PlaceRedisService {
    @Autowired
    private final RedisTemplate<String, Object> objectRedisTemplate;
    private final HashOperations<String, String, PlaceEntity> hashOperations;
    @Autowired
    public PlaceRedisService(RedisTemplate<String, Object> redisTemplate) {
        this.objectRedisTemplate = redisTemplate;
        this.hashOperations = redisTemplate.opsForHash();
    }
    public PlaceEntity save(PlaceEntity placeEntity) {
        String key = "places";  // 해시의 키
        hashOperations.put(key, placeEntity.getId(), placeEntity);
        return placeEntity;
    }

    public PlaceEntity findById(String placeId) {
        String key = "places";  // 해시의 키
        return hashOperations.get(key, placeId);
    }
}
