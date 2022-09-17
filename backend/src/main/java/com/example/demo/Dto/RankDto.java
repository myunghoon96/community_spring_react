package com.example.demo.Dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.ZSetOperations;

@Getter
@NoArgsConstructor
public class RankDto {

    private String value;
    private Double score;

    public RankDto(String value, Double score){
        this.value = value;
        this.score = score;
    }
}
