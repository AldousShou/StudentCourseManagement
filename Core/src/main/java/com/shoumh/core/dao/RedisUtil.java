package com.shoumh.core.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.StringJoiner;
import java.util.concurrent.TimeUnit;


@Component
public class RedisUtil {

    private final StringRedisTemplate redisTemplate;

    @Autowired
    public RedisUtil(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String concatKeys(String... keys) {
        if (keys == null) {
            throw new IllegalArgumentException("Keys array cannot be null");
        }

        StringJoiner stringJoiner = new StringJoiner(":");
        for (String key : keys) {
            if (key == null) {
                throw new IllegalArgumentException("Key cannot be null");
            }
            stringJoiner.add(key);
        }
        return stringJoiner.toString();
    }

    // 设置键值对，并指定过期时间
    public void setWithExpiration(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // 获取键对应的值
    public String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // 删除指定键
    public void delete(String key) {
        redisTemplate.delete(key);
    }

    // 检查指定键是否存在
    public boolean hasKey(String key) {
        return Boolean.TRUE.equals(redisTemplate.hasKey(key));
    }

    // 设置键值对
    public void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // decr num
    public Long decr(String key) {
        return redisTemplate.opsForValue().decrement(key);
    }

    // incr num
    public Long incr(String key) {
        return redisTemplate.opsForValue().increment(key);
    }
}
