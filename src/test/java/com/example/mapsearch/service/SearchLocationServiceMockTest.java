package com.example.mapsearch.service;

import com.example.mapsearch.dto.Place;
import com.example.mapsearch.dto.PlaceResDTO;
import com.example.mapsearch.dto.SerchPlaceResDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
public class SearchLocationServiceMockTest {
    @Autowired
    private SearchPlaceByKeywordService searchLocationService;

    @MockBean
    private KaKaoApiServiceImpl kaKaoApiService;
    @MockBean
    private NaverApiServiceImpl naverApiService;


    @Test
    void 카카오_지도_우선_정렬(){
        String exQuery = "곱창";
        Place kakaoPlace1 = new Place("A곱창", "127.0", "37.0");
        Place kakaoPlace2 = new Place("B곱창", "128.0", "38.0");
        Place kakaoPlace3 = new Place("C곱창", "129.0", "39.0");
        Place kakaoPlace4 = new Place("D곱창", "130.0", "40.0");
        Place kakaoPlace5 = new Place("AA곱창", "1277.0", "377.0");
        when(kaKaoApiService.callPlaceInfoApi(exQuery)).thenReturn(Arrays.asList(kakaoPlace1, kakaoPlace2, kakaoPlace3, kakaoPlace4, kakaoPlace5));

        Place naverPlace1 = new Place("A곱창", "127.0", "37.0");
        Place naverPlace2 = new Place("E곱창", "131.0", "41.0");
        Place naverPlace3 = new Place("D곱창", "130.0", "40.0");
        Place naverPlace4 = new Place("C곱창", "129.0", "39.0");
        Place naverPlace5 = new Place("EE곱창", "1311.0", "411.0");

        List<String> expectedPlacesTitle = Arrays.asList("A곱창", "C곱창", "D곱창", "B곱창", "AA곱창", "E곱창", "EE곱창");
        when(naverApiService.callPlaceInfoApi(exQuery)).thenReturn(Arrays.asList(naverPlace1, naverPlace2, naverPlace3, naverPlace4, naverPlace5));


        String query = "곱창";
        SerchPlaceResDTO serchLocationRes = searchLocationService.searchPlaceByKeyword(query);

        assertEquals(expectedPlacesTitle.size(), serchLocationRes.getPlaces().size());
        assertEquals(expectedPlacesTitle, serchLocationRes.getPlaces().stream().map(PlaceResDTO::getTitle).collect(Collectors.toList()));
    }

}
