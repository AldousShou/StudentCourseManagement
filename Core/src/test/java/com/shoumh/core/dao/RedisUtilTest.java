package com.shoumh.core.dao;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

@SpringBootTest
class RedisUtilTest {

    @Autowired
    private RedisUtil redisUtil;

    @Test
    void concatKeys() {
        String res = redisUtil.concatKeys("1", "2", "3");
        assert res.equals("1:2:3");
    }

    @Test
    void set() {
        redisUtil.set("test_key", "test_val");
        assert redisUtil.get("test_key") != null;
        redisUtil.delete("test_key");
    }

    @Test
    void setWithExpiration() {
        redisUtil.setWithExpiration("test_key2", "test_val", 10, TimeUnit.SECONDS);
        assert redisUtil.get("test_key2") != null;
        redisUtil.delete("test_key2");
    }

    @Test
    void hasKey() {
        redisUtil.set("test_key3", "test_val");
        assert redisUtil.hasKey("test_key3");
        redisUtil.delete("test_key3");
    }
}