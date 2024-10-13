package com.example.mapsearch.service;

import com.example.mapsearch.constant.Constant;
import com.example.mapsearch.dto.ExternalApiResult;
import com.example.mapsearch.dto.NaverApiResDTO;
import com.example.mapsearch.dto.NaverPlace;
import com.example.mapsearch.dto.Place;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.mapsearch.constant.Constant.REQUEST_COUNT;

@Service
public class NaverApiServiceImpl implements CallExternalApiService{

    private final RestTemplate restTemplate;

    // .env
    @Value("${naver.clinet.id}")
    private String naverClientId;

    @Value("${naver.clinet.SECRET}")
    private String naverClientSecret;


    public NaverApiServiceImpl(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }


    @Override
    public HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-Naver-Client-Id", naverClientId);
        headers.set("X-Naver-Client-Secret", naverClientSecret);
        return headers;
    }

    @Override
    public ExternalApiResult callPlaceInfoApi(String param, int page) {
        ExternalApiResult result = new ExternalApiResult();
        URI uri = UriComponentsBuilder
                .fromUriString("https://openapi.naver.com")
                .path("/v1/search/local.json")
                .queryParam("query", param)
                .queryParam("display", REQUEST_COUNT)
                .queryParam("start", page)
                .encode()
                .build()
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>(this.getAuthHeader());

        ResponseEntity<NaverApiResDTO> response = restTemplate.exchange(uri, HttpMethod.GET, entity, NaverApiResDTO.class);
        if(HttpStatusCode.valueOf(200).equals(response.getStatusCode())){
            NaverApiResDTO body = response.getBody();
            List<Place> naverPlaces = body.getItems().stream().map(this::toPlace).collect(Collectors.toList());
            result.setPlaceList(naverPlaces);
            result.setEnd(REQUEST_COUNT >= body.getTotal());
        }
        return result;
    }


    private Place toPlace(NaverPlace naverPlace){
        String title = naverPlace.getTitle().replaceAll("<[^>]+>", "");;
        DecimalFormat df = new DecimalFormat("#.####");

        String x = df.format(naverPlace.getMapx()/ 10000000.0);
        String y = df.format(naverPlace.getMapy()/ 10000000.0);
        return new Place(title, x, y);
    }
}
