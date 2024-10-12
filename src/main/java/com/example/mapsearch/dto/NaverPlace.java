package com.example.mapsearch.dto;

import lombok.Getter;

@Getter
public class NaverPlace {
    private String title;
    private String address;
    private String roadAddress;
    private Double mapx;
    private Double mapy;

    public NaverPlace(String title, Double mapx, Double mapy) {
        this.title = title;
        this.mapx = mapx;
        this.mapy = mapy;
    }

}
