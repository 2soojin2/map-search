package com.example.mapsearch.service;

import com.example.mapsearch.dto.ExternalApiResult;
import com.example.mapsearch.dto.Place;
import org.springframework.http.HttpHeaders;

import java.util.List;

public interface CallExternalApiService {

    HttpHeaders getAuthHeader();
    ExternalApiResult callPlaceInfoApi(String param, int page);
}
