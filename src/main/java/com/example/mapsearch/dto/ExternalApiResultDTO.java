package com.example.mapsearch.dto;

import com.example.mapsearch.domain.Place;
import lombok.Data;

import java.util.List;

@Data
public class ExternalApiResultDTO {
    private List<Place> placeList;
    private boolean isEnd;

    public ExternalApiResultDTO(List<Place> placeList, boolean isEnd) {
        this.placeList = placeList;
        this.isEnd = isEnd;
    }
}
