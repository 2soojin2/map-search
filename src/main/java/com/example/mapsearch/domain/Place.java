package com.example.mapsearch.domain;

import lombok.Getter;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

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
        if (this == obj) return true; // 동일 객체 참조
        if (obj == null || getClass() != obj.getClass()) return false; // null 체크 및 클래스 확인
        Place place = (Place) obj;
        return Objects.equals(normalize(title), normalize(place.title));
    }

    @Override
    public int hashCode() {
        return Objects.hash(title != null ? normalize(title): null);
    }

    private String normalize(String input) {
        return input != null ? input.replaceAll("[\\s()\\[\\]{}<>\\p{Punct}]", "") : ""; // 공백 및 특수문자 제거
    }
}
