package com.example.mapsearch.facade;

import com.example.mapsearch.entity.KeywordEntity;
import com.example.mapsearch.service.KeywordRedisService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;
import org.redisson.api.RLock;

@Slf4j
@Component
@AllArgsConstructor
public class RedissonLockKeywordFacade {
    private RedissonClient redissonClient;
    private KeywordRedisService keywordRedisService;

    public void saveOrUpdateKeyword(KeywordEntity keywordEntity){
        RLock lock = redissonClient.getLock(keywordEntity.getId());
        try{
            boolean available = lock.tryLock(10, 1, TimeUnit.SECONDS);
            if(!available){
                log.info("RedissonLockKeywordFacade :: saveOrUpdateKeyword >>>> Lock 획득 실패");
                return;
            }
            keywordRedisService.saveOrUpdateKeyword(keywordEntity);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }finally {
            lock.unlock();
        }
    }
}
