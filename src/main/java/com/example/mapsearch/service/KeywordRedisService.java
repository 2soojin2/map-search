package com.example.mapsearch.service;

import com.example.mapsearch.constant.Constant;
import com.example.mapsearch.dto.KeywordRankResDTO;
import com.example.mapsearch.entity.KeywordEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class KeywordRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;
    private final HashOperations<String, String, KeywordEntity> keywordHashOperations;

    public KeywordEntity saveKeyword(KeywordEntity keywordEntity) {
        keywordHashOperations.put(Constant.REDIS_HASH_KEY_KEWORDS, keywordEntity.getTitle(), keywordEntity);
        return keywordEntity;
    }

    public void saveOrUpdateKeyword(KeywordEntity keywordEntity) {
        KeywordEntity existingKeyword = this.findByTitle(keywordEntity.getTitle());
        KeywordEntity savedKeyword = new KeywordEntity();
        if (ObjectUtils.isEmpty(existingKeyword)) {
            this.saveKeywordSearchCount(keywordEntity.getTitle(), keywordEntity.getSearchCount());
            savedKeyword = keywordEntity;
        } else {
            existingKeyword.plusOneSearchCount();
            this.incrementKeywordSearchCount(existingKeyword.getTitle());
            savedKeyword = existingKeyword;
        }
        this.saveKeyword(savedKeyword);
    }

    public KeywordEntity findByTitle(String keyword) {
//        KEYWORD_SEARCH_COUNT
        return keywordHashOperations.get(Constant.REDIS_HASH_KEY_KEWORDS, keyword);
    }

    public void saveKeywordSearchCount(String keyword, double searchCount) {
        zSetOperations.add(Constant.KEYWORD_SEARCH_COUNT, keyword, searchCount);
    }

    public void incrementKeywordSearchCount(String keyword) {
        zSetOperations.incrementScore(Constant.KEYWORD_SEARCH_COUNT, keyword, 1.0);
    }

    public Double getKeyWordSearchCount(String keyword) {
        return zSetOperations.score(Constant.KEYWORD_SEARCH_COUNT, keyword);
    }

    public List<KeywordRankResDTO> getKeywordRanking(){
        List<KeywordRankResDTO> result = new ArrayList<>();
        Set<ZSetOperations.TypedTuple<Object>> keywordTuples = zSetOperations.reverseRangeWithScores(Constant.KEYWORD_SEARCH_COUNT, Constant.SEARCH_START, Constant.SEARCH_END);
        for (ZSetOperations.TypedTuple<Object> stringTypedTuple : keywordTuples) {
            String keyword = stringTypedTuple.getValue().toString();
            Double searchCount = stringTypedTuple.getScore();
            KeywordRankResDTO keywordRankResDTO = new KeywordRankResDTO(keyword, searchCount);
            result.add(keywordRankResDTO);
        }
        return result;
    }
}
