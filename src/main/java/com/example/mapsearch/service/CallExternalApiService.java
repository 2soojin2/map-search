package com.example.mapsearch.service;

import com.example.mapsearch.dto.Place;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface CallExternalApiService {

    HttpHeaders getAuthHeader();
    List<Place> callPlaceInfoApi(String param);
    // 다음 페이지 받아오기

}
