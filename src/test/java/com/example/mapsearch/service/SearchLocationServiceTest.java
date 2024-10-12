package com.example.mapsearch.service;

import com.example.mapsearch.dto.Place;
import com.example.mapsearch.dto.SerchPlaceResDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class SearchLocationServiceTest {
    @Autowired
    private SearchPlaceByKeywordService searchPlaceService;

    @Test
    void 카카오_네이버_모두_검색_결과가_없을때(){
        String query = "aslkdjkfj";
        List<Place> expectedResult = new ArrayList<>();

        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(expectedResult.size(), serchLocationRes.getPlaces().size());
    }


    @Test
    void 최초_조회결과가_10개_미만일때(){

    }

    @Test
    void 정상적으로_결과가_조회됨(){
        String query = "곱창";
        SerchPlaceResDTO serchLocationRes = searchPlaceService.searchPlaceByKeyword(query);
        assertEquals(10, serchLocationRes.getPlaces().size());
    }

}