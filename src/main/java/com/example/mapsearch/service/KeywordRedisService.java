package com.example.mapsearch.service;

import com.example.mapsearch.constant.Constant;
import com.example.mapsearch.entity.KeywordEntity;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.Map;

@Service
@AllArgsConstructor
public class KeywordRedisService {

    private final RedisTemplate<String, Object> redisTemplate;
    private ZSetOperations<String, Object> zSetOperations;
    private final HashOperations<String, String, KeywordEntity> keywordHashOperations;

    public KeywordEntity saveKeyword(KeywordEntity keywordEntity) {
        keywordHashOperations.put(Constant.REDIS_HASH_KEY_KEWORDS, keywordEntity.getId(), keywordEntity);
        return keywordEntity;
    }

    public KeywordEntity saveOrUpdateKeyword(KeywordEntity keywordEntity) {
        KeywordEntity existingKeyword = this.findByTitle(keywordEntity);
        KeywordEntity savedKeyword = new KeywordEntity();
        if (ObjectUtils.isEmpty(existingKeyword)) {
            this.saveKeywordSearchCount(keywordEntity.getId(), keywordEntity.getSearchCount());
            savedKeyword = keywordEntity;
        } else {
            existingKeyword.plusOneSearchCount();
            this.incrementKeywordSearchCount(existingKeyword.getId());
            savedKeyword = existingKeyword;
        }
        this.saveKeyword(savedKeyword);
        return savedKeyword;
    }

    public KeywordEntity findByTitle(KeywordEntity keywordEntity) {
        Map<String, KeywordEntity> keywordEntities = keywordHashOperations.entries(Constant.REDIS_HASH_KEY_KEWORDS);
        for (KeywordEntity keyword : keywordEntities.values()) {
            if (keyword.equals(keywordEntity)) {
                return keyword;
            }
        }
        return null;
    }

    public KeywordEntity findById(String keywordId) {
        return keywordHashOperations.get(Constant.REDIS_HASH_KEY_KEWORDS, keywordId);
    }

    public void saveKeywordSearchCount(String keywordId, double searchCount) {
        zSetOperations.add(Constant.KEYWORD_SEARCH_COUNT, keywordId, searchCount);
    }

    public void incrementKeywordSearchCount(String keywordId) {
        zSetOperations.incrementScore(Constant.KEYWORD_SEARCH_COUNT, keywordId, 1.0);
    }

    public Double getKeyWordSearchCount(String keywordId) {
        return zSetOperations.score(Constant.KEYWORD_SEARCH_COUNT, keywordId);
    }
}
