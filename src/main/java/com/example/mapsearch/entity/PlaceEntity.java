package com.example.mapsearch.entity;

import com.example.mapsearch.dto.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "places")
public class PlaceEntity implements Serializable {

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

    public void setSearchCount(int searchCount) {
        this.searchCount = searchCount;
    }

    public void plusOneSearchCount(){
        this.searchCount += 1;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        PlaceEntity place = (PlaceEntity) obj;
        return Objects.equals(x, place.x) &&
                Objects.equals(y, place.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
