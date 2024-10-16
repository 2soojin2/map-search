package com.example.mapsearch.service;

import com.example.mapsearch.dto.ExternalApiResultDTO;
import com.example.mapsearch.domain.Place;
import com.example.mapsearch.dto.PlaceResDTO;
import com.example.mapsearch.dto.SerchPlaceResDTO;
import com.example.mapsearch.entity.KeywordEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.mapsearch.constant.Constant.MAX_REQUEST_COUNT;
import static com.example.mapsearch.constant.Constant.REQUEST_COUNT;


@Service
@AllArgsConstructor
public class SearchPlaceByKeywordService {

    private final KaKaoApiServiceImpl kakaoApiService;
    private final NaverApiServiceImpl naverApiService;
    private final KeywordRedisService keywordRedisService;

    public SerchPlaceResDTO searchPlaceByKeyword(String query) {
        SerchPlaceResDTO result = new SerchPlaceResDTO();
        int page = 1;

        ExternalApiResultDTO externalApiResultByKaKao = kakaoApiService.callPlaceInfoApi(query, page);
        List<Place> kakaoPlaces = externalApiResultByKaKao.getPlaceList();
        ExternalApiResultDTO externalApiResultByNaver = naverApiService.callPlaceInfoApi(query, page);
        List<Place> naverPlaces = externalApiResultByNaver.getPlaceList();

        page += 1;

        // 카카오가 부족할 경우
        if (kakaoPlaces.size() < REQUEST_COUNT && !externalApiResultByNaver.isEnd()){
            // 네이버 다음 페이지 호출
            ExternalApiResultDTO nextPageByNaver = naverApiService.callPlaceInfoApi(query, page);
            naverPlaces.addAll(nextPageByNaver.getPlaceList());
        }
        // 네이버가 부족할 경우
        if(naverPlaces.size() < REQUEST_COUNT && !externalApiResultByKaKao.isEnd()){
            // 카카오 다음 페이지 호출
            ExternalApiResultDTO nextPageByKakao = kakaoApiService.callPlaceInfoApi(query, page);
            naverPlaces.addAll(nextPageByKakao.getPlaceList());
        }

        List<Place> sortedPlaces = this.mergeAndSortPlaces(kakaoPlaces, naverPlaces);
        result.setPlaces(sortedPlaces.stream().map(place -> new PlaceResDTO(place.getTitle()))
                        .limit(MAX_REQUEST_COUNT)
                .collect(Collectors.toList()));

        KeywordEntity keywordEntity = new KeywordEntity(query);
        keywordRedisService.saveOrUpdateKeyword(keywordEntity);
        return result;
    }

    private static List<Place> mergeAndSortPlaces(List<Place> kakaoPlaces, List<Place> naverPlaces) {
        List<Place> both = kakaoPlaces.stream().distinct().filter(o->naverPlaces.contains(o)).collect(Collectors.toList());
        List<Place> kakaoOnly = kakaoPlaces.stream().distinct().filter(o->!both.contains(o)).collect(Collectors.toList());
        List<Place> naverOnly = naverPlaces.stream().distinct().filter(o->!both.contains(o)).collect(Collectors.toList());


        List<Place> finalResult = new ArrayList<>();
        finalResult.addAll(both);
        finalResult.addAll(kakaoOnly);
        finalResult.addAll(naverOnly);

        return finalResult;
    }
}




