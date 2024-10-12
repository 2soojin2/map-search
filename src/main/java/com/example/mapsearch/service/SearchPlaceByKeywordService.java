package com.example.mapsearch.service;

import com.example.mapsearch.dto.Place;
import com.example.mapsearch.dto.PlaceResDTO;
import com.example.mapsearch.dto.SerchPlaceResDTO;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class SearchPlaceByKeywordService {

    private final KaKaoApiServiceImpl kaKaoApiService;
    private final NaverApiServiceImpl naverApiService;

    public SerchPlaceResDTO searchPlaceByKeyword(String query) {
        SerchPlaceResDTO result = new SerchPlaceResDTO();


        List<Place> kakaoPlaces = kaKaoApiService.callPlaceInfoApi(query);
        List<Place> naverPlaces = naverApiService.callPlaceInfoApi(query);

        List<PlaceResDTO> sortedPlaces = this.mergeAndSortPlaces(kakaoPlaces, naverPlaces)
                .stream().map(place -> new PlaceResDTO(place.getTitle()))
                .collect(Collectors.toList());

        result.setPlaces(sortedPlaces);
        return result;
    }

    private static List<Place> mergeAndSortPlaces(List<Place> kakaoPlaces, List<Place> naverPlaces) {
        List<Place> both = kakaoPlaces.stream().filter(o->naverPlaces.contains(o)).collect(Collectors.toList());
        List<Place> kakaoOnly = kakaoPlaces.stream().filter(o->!both.contains(o)).collect(Collectors.toList());
        List<Place> naverOnly = naverPlaces.stream().filter(o->!both.contains(o)).collect(Collectors.toList());


        List<Place> finalResult = new ArrayList<>();
        finalResult.addAll(both);
        finalResult.addAll(kakaoOnly);
        finalResult.addAll(naverOnly);

        return finalResult;
    }


}




