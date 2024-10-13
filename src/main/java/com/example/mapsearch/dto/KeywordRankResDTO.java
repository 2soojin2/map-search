package com.example.mapsearch.dto;

import lombok.Getter;

@Getter
public class KeywordRankResDTO {
    private String keyword;
    private double searchCount;

    public KeywordRankResDTO(String keyword, double searchCount) {
        this.keyword = keyword;
        this.searchCount = searchCount;
    }
}
