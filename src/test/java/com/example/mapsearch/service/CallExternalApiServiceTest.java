package com.example.mapsearch.service;

import com.example.mapsearch.dto.ExternalApiResultDTO;
import com.example.mapsearch.domain.Place;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class CallExternalApiServiceTest {
    @Autowired
    private KaKaoApiServiceImpl kaKaoApiService;
    @Autowired
    private NaverApiServiceImpl naverApiService;
    @Test
    void 카카오_정상적으로_연동(){
        String query = "우리은행 상암DMC금융센터";
        ExternalApiResultDTO externalApiResult = kaKaoApiService.callPlaceInfoApi(query, 1);
        List<Place> kakaoPlaces =   externalApiResult.getPlaceList();
        boolean isEnd = externalApiResult.isEnd();
        Assertions.assertEquals(5, kakaoPlaces.size());
        Assertions.assertEquals(true, isEnd);
    }

    @Test
    void 네이버_정상적으로_연동(){
        String query = "우리은행 상암DMC금융센터";
        ExternalApiResultDTO externalApiResult = naverApiService.callPlaceInfoApi(query, 1);
        boolean isEnd = externalApiResult.isEnd();
        Assertions.assertEquals(2, externalApiResult.getPlaceList().size());
        Assertions.assertEquals(true, isEnd);
    }

}