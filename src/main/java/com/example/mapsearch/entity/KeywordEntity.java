package com.example.mapsearch.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash(value = "keywords")
public class KeywordEntity implements Serializable {
    @Id
    private String id;
    private String title;
    private int searchCount;

    public KeywordEntity(String title) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.searchCount = 1;
    }

    public KeywordEntity(String title, int searchCount) {
        this.id = UUID.randomUUID().toString();
        this.title = title;
        this.searchCount = searchCount;
    }


    public void plusOneSearchCount(){
        this.searchCount += 1;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true; // 동일 객체 체크
        if (!(o instanceof KeywordEntity)) return false; // 타입 체크
        KeywordEntity that = (KeywordEntity) o; // 타입 변환
        return title != null && title.equals(that.title); // title 문자열 비교
    }

    @Override
    public int hashCode() {
        return title != null ? title.hashCode() : 0; // title을 해시코드로 사용
    }
}
