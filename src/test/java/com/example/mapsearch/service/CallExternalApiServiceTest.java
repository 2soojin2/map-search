package com.example.mapsearch.service;

import com.example.mapsearch.dto.ExternalApiResult;
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
        ExternalApiResult externalApiResult = kaKaoApiService.callPlaceInfoApi(query, 1);
        List<Place> kakaoPlaces =   externalApiResult.getPlaceList();
        boolean isEnd = externalApiResult.isEnd();
        Assertions.assertEquals(5, kakaoPlaces.size());
        Assertions.assertEquals(true, isEnd);
    }

    @Test
    void 네이버_정상적으로_연동_5개미만(){
        String query = "우리은행 상암DMC금융센터";
        ExternalApiResult externalApiResult = naverApiService.callPlaceInfoApi(query, 1);
        boolean isEnd = externalApiResult.isEnd();
        Assertions.assertEquals(2, externalApiResult.getPlaceList().size());
        Assertions.assertEquals(true, isEnd);
    }

    @Test
    void 네이버_정상적으로_연동_5개이상(){
        // TODO 네이버의 경우 total 최대 값이 5
//        String query = "우리은행";
//        ExternalApiResult externalApiResult = naverApiService.callPlaceInfoApi(query);
//        boolean isEnd = externalApiResult.isEnd();
//        Assertions.assertEquals(5, externalApiResult.getPlaceList().size());
//        Assertions.assertEquals(false, isEnd);
    }
}