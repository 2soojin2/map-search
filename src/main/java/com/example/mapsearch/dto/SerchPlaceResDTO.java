package com.example.mapsearch.dto;


import lombok.Getter;

import java.util.List;
import java.util.Map;

@Getter
public class SerchPlaceResDTO {
    private List<PlaceResDTO> places;

    public void setPlaces(List<PlaceResDTO> places) {
        this.places = places;
    }
}
