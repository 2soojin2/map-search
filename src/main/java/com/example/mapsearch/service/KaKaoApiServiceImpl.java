package com.example.mapsearch.service;

import com.example.mapsearch.constant.ApiConstants;
import com.example.mapsearch.dto.KakaoApiResDTO;
import com.example.mapsearch.dto.KakaoPlace;
import com.example.mapsearch.dto.Place;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Service
public class KaKaoApiServiceImpl implements CallExternalApiService{
    private final RestTemplate restTemplate;

    public KaKaoApiServiceImpl(RestTemplateBuilder builder) {
        restTemplate = builder.build();
    }

    //.env
    @Value("${kakao.secret.key}")
    private String kakaoSecretKey;

    @Override
    public HttpHeaders getAuthHeader() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "KakaoAK " + kakaoSecretKey);
        return headers;
    }

    @Override
    public List<Place> callPlaceInfoApi(String param) {
        URI uri = UriComponentsBuilder
                .fromUriString("https://dapi.kakao.com")
                .path("/v2/local/search/keyword.json")
                .queryParam("query", param)
                .queryParam("size", ApiConstants.requestCount)
                .encode()
                .build()
                .toUri();

        HttpEntity<String> entity = new HttpEntity<>(this.getAuthHeader());

        List<Place> result = new ArrayList<>();
        ResponseEntity<KakaoApiResDTO> response = restTemplate.exchange(uri, HttpMethod.GET, entity, KakaoApiResDTO.class);
        if(HttpStatusCode.valueOf(200).equals(response.getStatusCode())){
            KakaoApiResDTO body = response.getBody();
            result = body.getDocuments().stream().map(this::toPlace).collect(Collectors.toList());
        }else{
            log.error("카카오 연동 에러");
        }
        return result;
    }

    private Place toPlace(KakaoPlace kakaoPlace){

        DecimalFormat df = new DecimalFormat("#.####");
        String formattedX = df.format(kakaoPlace.getX()); // .을 없애고 소수 세자리 까지만
        String formattedY = df.format(kakaoPlace.getY()); // .을 없애고 소수 세자리 까지만

        Place place = new Place(kakaoPlace.getPlace_name(), formattedX, formattedY);
        return place;
    }
}
