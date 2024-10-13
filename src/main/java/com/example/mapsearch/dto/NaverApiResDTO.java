package com.example.mapsearch.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class NaverApiResDTO {
    private List<NaverPlace> items;
    private int total;
}
