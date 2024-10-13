package com.example.mapsearch.service;

import com.example.mapsearch.constant.Constant;
import com.example.mapsearch.entity.KeywordEntity;
import com.example.mapsearch.entity.PlaceEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Service
@AllArgsConstructor
public class PlaceRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;
    private final HashOperations<String, String, PlaceEntity> hashOperations;



    public PlaceEntity savePlace(PlaceEntity placeEntity) {
        hashOperations.put(Constant.REDIS_HASH_KEY_PLACES, placeEntity.getId(), placeEntity);
        return placeEntity;
    }

    public PlaceEntity saveOrUpdatePlace(PlaceEntity newPlace) {
        PlaceEntity existingPlace = this.findByCoordinates(newPlace);
        PlaceEntity savedPlace = new PlaceEntity();
        if (ObjectUtils.isEmpty(existingPlace)) {
            this.savePlaceSearchCount(newPlace.getId(), newPlace.getSearchCount());
            savedPlace = newPlace;
            this.savePlace(newPlace);
        } else {
            existingPlace.plusOneSearchCount();
            this.incrementPlaceSearchCount(existingPlace.getId());
            savedPlace = existingPlace;
            this.savePlace(existingPlace);
        }
        return savedPlace;
    }

    public PlaceEntity findByCoordinates(PlaceEntity place) {
        Map<String, PlaceEntity> allPlaces =
                hashOperations.entries(Constant.REDIS_HASH_KEY_PLACES);

        for (PlaceEntity findplace : allPlaces.values()) {
            if (findplace.equals(place)) {
                return findplace;
            }
        }
        return null;
    }

    public PlaceEntity findById(String placeId) {
        return hashOperations.get(Constant.REDIS_HASH_KEY_PLACES, placeId);
    }

    public void savePlaceSearchCount(String placeId, double searchCount) {
        zSetOperations.add(Constant.PLACE_SEARCH_COUNT, placeId, searchCount);
    }

    public void incrementPlaceSearchCount(String placeId) {
        zSetOperations.incrementScore(Constant.PLACE_SEARCH_COUNT, placeId, 1.0);
    }

    public Double getPlaceSearchCount(String placeId) {
        return zSetOperations.score(Constant.PLACE_SEARCH_COUNT, placeId);
    }
}
