package com.example.mapsearch.service;

import com.example.mapsearch.dto.ExternalApiResultDTO;
import org.springframework.http.HttpHeaders;

public interface CallExternalApiService {

    HttpHeaders getAuthHeader();
    ExternalApiResultDTO callPlaceInfoApi(String param, int page);
}
