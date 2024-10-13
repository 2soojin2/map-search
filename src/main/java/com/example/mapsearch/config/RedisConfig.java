package com.example.mapsearch.config;

import com.example.mapsearch.entity.KeywordEntity;
import com.example.mapsearch.entity.PlaceEntity;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private int port;

    @Bean
    LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // 키는 String으로 직렬화, 값은 JSON 형식으로 직렬화
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());

        // 해시 키와 값도 각각 String과 JSON 직렬화
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }

    @Bean
    public HashOperations<String, String, PlaceEntity> placeHashOperations(RedisTemplate<String, Object> objectRedisTemplate) {
        return objectRedisTemplate.opsForHash();
    }

    @Bean
    public HashOperations<String, String, KeywordEntity> keywordHashOperations(RedisTemplate<String, Object> objectRedisTemplate) {
        return objectRedisTemplate.opsForHash();
    }

    @Bean
    public ZSetOperations<String, Object> zSetOperations(RedisTemplate<String, Object> objectRedisTemplate) {
        return objectRedisTemplate.opsForZSet();
    }
}
