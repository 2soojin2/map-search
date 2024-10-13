package com.example.mapsearch.dto;

import lombok.Data;

import java.util.List;

@Data
public class ExternalApiResult {
    private List<Place> placeList;
    private boolean isEnd;
}
