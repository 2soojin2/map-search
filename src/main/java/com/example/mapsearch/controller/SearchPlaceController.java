package com.example.mapsearch.controller;

import com.example.mapsearch.dto.SerchPlaceResDTO;
import com.example.mapsearch.entity.PlaceEntity;
import com.example.mapsearch.service.SearchPlaceByKeywordService;
import com.example.mapsearch.service.SearchPlaceBySearchCountService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/v1")
public class SearchPlaceController {

    private final SearchPlaceByKeywordService searchPlaceByKeywordService;
    private final SearchPlaceBySearchCountService searchPlaceBySearchCountService;

    @GetMapping(value = "/place")
    public SerchPlaceResDTO searchPlaceByKeyword(@RequestParam String param){
        return searchPlaceByKeywordService.searchPlaceByKeyword(param);
    }

    @GetMapping(value = "/ranking")
    public List<PlaceEntity> searchPlace(){
        return searchPlaceBySearchCountService.getPlaceRanking();
    }
}
