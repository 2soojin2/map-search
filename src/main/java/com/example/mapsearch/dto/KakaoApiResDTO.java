package com.example.mapsearch.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class KakaoApiResDTO {
    List<KakaoPlaceDTO> documents;
    KaKaoMetaData meta;
}
