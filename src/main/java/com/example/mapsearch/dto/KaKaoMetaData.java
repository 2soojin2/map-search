package com.example.mapsearch.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
public class KaKaoMetaData {
    @JsonProperty("is_end")
    private boolean isEnd;
}
