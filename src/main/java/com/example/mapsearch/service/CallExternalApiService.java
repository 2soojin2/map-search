package com.example.mapsearch.service;

import com.example.mapsearch.dto.ExternalApiResult;
import org.springframework.http.HttpHeaders;

public interface CallExternalApiService {

    HttpHeaders getAuthHeader();
    ExternalApiResult callPlaceInfoApi(String param, int page);
}
