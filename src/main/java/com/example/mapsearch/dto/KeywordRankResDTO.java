package com.example.mapsearch.dto;

import lombok.Getter;

@Getter
public class KeywordRankResDTO {
    private String keyword;
    private Double searchCount;

    public KeywordRankResDTO(String keyword, Double searchCount) {
        this.keyword = keyword;
        this.searchCount = searchCount;
    }
}
