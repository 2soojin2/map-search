package com.example.mapsearch.dto;

import lombok.Getter;

@Getter
public class KakaoPlaceDTO {
    private String address_name;
    private String place_name;
    private String road_address_name;
    private Double x;
    private Double y;

    public KakaoPlaceDTO(String place_name, Double x, Double y) {
        this.place_name = place_name;
        this.x = x;
        this.y = y;
    }
}
