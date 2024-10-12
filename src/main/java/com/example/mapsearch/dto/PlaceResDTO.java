package com.example.mapsearch.dto;

import lombok.Getter;

@Getter
public class PlaceResDTO {
    private String title;

    public PlaceResDTO(String title) {
        this.title = title;
    }
}
