package com.example.mapsearch.service;

import com.example.mapsearch.entity.PlaceEntity;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PlaceRedisService {
    @Autowired
    private RedisTemplate<String, String> redisTemplate;
    private final ObjectMapper objectMapper;

    public PlaceEntity save(PlaceEntity place) {
        try {
            String jsonString = objectMapper.writeValueAsString(place);
            redisTemplate.opsForValue().set(place.getId(), jsonString);
            return place;
        }catch (JsonProcessingException e){
            e.printStackTrace();
        }
        return null;
        // RDB 저장 요청 (옵션)
        // Redis의 SAVE 명령어를 직접 호출할 수는 없지만,
        // 주기적으로 RDB가 저장되도록 Redis 설정을 해두면 된다.
        // redisTemplate.getConnectionFactory().getConnection().save();
    }

    public PlaceEntity findById(String placeId) {
        String jsonString = redisTemplate.opsForValue().get(placeId);
        if (jsonString != null) {
            try {
                return objectMapper.readValue(jsonString, PlaceEntity.class);
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
