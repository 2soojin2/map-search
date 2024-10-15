package com.example.mapsearch.controller;

import com.example.mapsearch.dto.KeywordRankResDTO;
import com.example.mapsearch.dto.SerchPlaceResDTO;
import com.example.mapsearch.service.SearchPlaceByKeywordService;
import com.example.mapsearch.service.SearchPlaceRankingService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
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
    private final SearchPlaceRankingService searchPlaceBySearchCountService;

    @GetMapping(value = "/place")
    public ResponseEntity<SerchPlaceResDTO> searchPlaceByKeyword(@RequestParam String param){
        SerchPlaceResDTO serchPlaceResDTO = searchPlaceByKeywordService.searchPlaceByKeyword(param);
        return ResponseEntity.ok(serchPlaceResDTO);
    }

    @GetMapping(value = "/ranking")
    public ResponseEntity<List<KeywordRankResDTO>> searchKeywordRanking(){
        List<KeywordRankResDTO> keywordRanking = searchPlaceBySearchCountService.getKeywordRanking();
        return ResponseEntity.ok(keywordRanking);
    }
}
