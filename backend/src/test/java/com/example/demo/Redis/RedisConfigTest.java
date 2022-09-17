package com.example.demo.Redis;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class RedisConfigTest {

    @Autowired
    RedisTemplate<String, String> redisTemplate;

    @Test
    @DisplayName("redis 연결 성공")
    void redisTest() {
        String key = "sample_key";
        String data = "sample_data";

        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(key, data);

        String s = valueOperations.get(key);
        Assertions.assertThat(s).isEqualTo(data);
    }
}