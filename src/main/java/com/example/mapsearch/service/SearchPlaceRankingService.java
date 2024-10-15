package com.example.mapsearch.service;

import com.example.mapsearch.constant.Constant;
import com.example.mapsearch.dto.KeywordRankResDTO;
import com.example.mapsearch.entity.KeywordEntity;
import com.example.mapsearch.entity.PlaceEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class SearchPlaceRankingService {

    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;
    private final PlaceRedisService placeRedisSerivce;
    private final KeywordRedisService keywordRedisService;

    private final int START = 0;
    private final int END = 9;

    public List<PlaceEntity> getPlaceRanking() {
        List<PlaceEntity> result = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> placeIdAndSearchCount = zSetOperations.reverseRangeWithScores(Constant.PLACE_SEARCH_COUNT, START, END);
        for (ZSetOperations.TypedTuple<Object> stringTypedTuple : placeIdAndSearchCount) {
            String placeId = stringTypedTuple.getValue().toString();
            PlaceEntity place = placeRedisSerivce.findById(placeId);
            result.add(place);
        }
        return result;
    }

    public List<KeywordRankResDTO> getKeywordRanking() {
        List<KeywordRankResDTO> result = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> keywordTuples = zSetOperations.reverseRangeWithScores(Constant.KEYWORD_SEARCH_COUNT, START, END);
        for (ZSetOperations.TypedTuple<Object> stringTypedTuple : keywordTuples) {
            String title = stringTypedTuple.getValue().toString();
            KeywordEntity keyword = keywordRedisService.findByTitle(title);
            KeywordRankResDTO keywordRankResDTO = new KeywordRankResDTO(keyword.getTitle(), keyword.getSearchCount());
            result.add(keywordRankResDTO);
        }
        return result;
    }
}
