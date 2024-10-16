package com.example.mapsearch.service;

import com.example.mapsearch.domain.Place;
import com.example.mapsearch.dto.ExternalApiResultDTO;
import com.example.mapsearch.dto.SerchPlaceResDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class SearchLocationServiceTest {
    @Autowired
    private SearchPlaceByKeywordService searchPlaceService;
    @MockBean
    private KaKaoApiServiceImpl kakaoApiService;
    @MockBean
    private NaverApiServiceImpl naverApiService;

    private List<Place> makeFakePlaceData(String query,int startX, int startY, int count) {
        List<Place> placeList = new ArrayList<>();
        for (int i = 0; i < count; i++) {
            Place place1 = new Place(query+(char)(i+65), String.valueOf(startX+i), String.valueOf(startY+i));
            placeList.add(place1);
        }
        return placeList;
    }
    @Test
    void 카카오_네이버_모두_검색_결과가_없을때(){
        String query = "aslkdjkfj";
        int page = 1;
        when(kakaoApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(new ArrayList<>(), true));
        when(naverApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(new ArrayList<>(), true));

        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(0, serchLocationRes.getPlaces().size());
    }

    @Test
    void 정상적으로_결과가_조회됨(){
        int page = 1;
        String query = "곱창";
        List<Place> kakaoMockData = makeFakePlaceData(query, 123, 456, 5);
        when(kakaoApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(kakaoMockData, true));
        List<Place> naverMockData = makeFakePlaceData(query, 456, 789, 5);
        when(naverApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(naverMockData, true));

        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(10, serchLocationRes.getPlaces().size());
    }



    @Test
    void 카카오가_5개_미만의_결과(){
        int page = 1;
        String query = "곱창";
        // 카카오 5개 미만 목
        List<Place> kakaoMockData = makeFakePlaceData(query, 123, 456, 4);
        when(kakaoApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(kakaoMockData, true));
        // 네이버 5개 이상 목
        List<Place> naverMockData = makeFakePlaceData(query, 456, 789, 5);
        when(naverApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(naverMockData, false));
        // 네이버 다음 페이지 목
        List<Place> naverMockDataNextPage = makeFakePlaceData(query, 101, 213, 5);
        when(naverApiService.callPlaceInfoApi(query, page+1)).thenReturn(new ExternalApiResultDTO(naverMockDataNextPage, true));

        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(10, serchLocationRes.getPlaces().size());
    }

    @Test
    void 네이버가_5개_미만의_결과(){
        int page = 1;
        String query = "곱창";
        // 카카오 5개 이상 목
        List<Place> kakoMockData = makeFakePlaceData(query, 456, 789, 5);
        when(kakaoApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(kakoMockData, false));
        // 네이버 다음 페이지 목
        List<Place> kakaoMockDataNextPage = makeFakePlaceData(query, 101, 213, 5);
        when(kakaoApiService.callPlaceInfoApi(query, page+1)).thenReturn(new ExternalApiResultDTO(kakaoMockDataNextPage, true));

        // 네이버 5개 미만 목
        List<Place> naverMockData = makeFakePlaceData(query, 123, 456, 4);
        when(naverApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(naverMockData, true));

        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(10, serchLocationRes.getPlaces().size());
    }

    @Test
    void 둘다_5개_미만의_결과(){
        int page = 1;
        String query = "곱창";
        List<Place> kakaoMockData = makeFakePlaceData(query, 123, 456, 3);
        when(kakaoApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(kakaoMockData, true));
        List<Place> naverMockData = makeFakePlaceData(query, 456, 789, 3);
        when(naverApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(naverMockData, true));
        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(6, serchLocationRes.getPlaces().size());
    }

    @Test
    void 이름이_같은_경우_같은_장소로_판별(){
        int page = 1;
        String query = "곱창";
        List<Place> kakaoMockData = makeFakePlaceData(query, 123, 456, 1);
        when(kakaoApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(kakaoMockData, true));
        List<Place> naverMockData = makeFakePlaceData(query, 456, 789, 1);
        when(naverApiService.callPlaceInfoApi(query, page)).thenReturn(new ExternalApiResultDTO(naverMockData, true));

        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(1, serchLocationRes.getPlaces().size());
    }

}