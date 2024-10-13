package com.example.mapsearch.dto;

import lombok.Getter;

import java.util.Objects;

@Getter
public class Place {
    private String title;
    private String x;
    private String y;
    private boolean isEnd;

    public Place(String title, String x, String y) {
        this.title = title;
        this.x = x;
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Place place = (Place) obj;
        return Objects.equals(x, place.x) &&
                Objects.equals(y, place.y);
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }
}
