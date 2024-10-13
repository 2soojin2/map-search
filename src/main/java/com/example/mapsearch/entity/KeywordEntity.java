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
        this.searchCount = 0;
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
        if (this == o) return true;
        if (!(o instanceof KeywordEntity)) return false;
        KeywordEntity that = (KeywordEntity) o;
        return Objects.equals(title, that.title);
    }

    @Override
    public int hashCode() {
        return Objects.hash(title);
    }
}
