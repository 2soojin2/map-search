package com.example.mapsearch.service;

import com.example.mapsearch.dto.KakaoPlace;
import com.example.mapsearch.dto.NaverPlace;
import com.example.mapsearch.dto.Place;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class CallExternalApiServiceTest {
    @Autowired
    private KaKaoApiServiceImpl kaKaoApiService;
    @Autowired
    private NaverApiServiceImpl naverApiService;
    @Test
    void 카카오_정상적으로_연동(){
        String query = "은행";
        List<Place> kakaoPlaces = kaKaoApiService.callPlaceInfoApi(query);
        Assertions.assertEquals(5, kakaoPlaces.size());
    }

    @Test
    void 네이버_정상적으로_연동(){
        String query = "은행";
        List<Place> naverPlaces = naverApiService.callPlaceInfoApi(query);
        Assertions.assertEquals(5, naverPlaces.size());
    }
}