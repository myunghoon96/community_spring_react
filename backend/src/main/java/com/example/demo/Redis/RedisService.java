package com.example.demo.Redis;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class RedisService {

    private final RedisTemplate<String, Boolean> redisTemplate;
    public RedisService(RedisTemplate<String, Boolean> redisTemplate){
        this.redisTemplate = redisTemplate;
    }

    public boolean isFirstViewByEmail(String email, Long boardId) {
        String key = "View history " + "Email:" + email + " BoardId:" + boardId;
        if (redisTemplate.hasKey(key)) {
            return false;
        }
        redisTemplate.opsForValue().set(key, true);
        redisTemplate.expire(key, 1, TimeUnit.DAYS);
        return true;
    }

}
