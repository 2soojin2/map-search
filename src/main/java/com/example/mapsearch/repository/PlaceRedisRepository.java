package com.example.mapsearch.repository;

import com.example.mapsearch.entity.PlaceEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRedisRepository extends CrudRepository<PlaceEntity, String> {
}
