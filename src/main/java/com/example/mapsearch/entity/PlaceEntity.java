package com.example.mapsearch.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.util.UUID;

@Getter
@RedisHash(value = "places")
public class PlaceEntity {

    @Id
    private String id;
    private String title;
    private String x;
    private String y;
    private int searchCount;

    public PlaceEntity(String title, String x, String y, int searchCount) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.x = x;
        this.y = y;
        this.searchCount = searchCount;
    }
}
