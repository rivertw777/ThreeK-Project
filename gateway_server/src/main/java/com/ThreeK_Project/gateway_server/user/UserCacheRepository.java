package com.ThreeK_Project.gateway_server.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, UserCache> redisTemplate;

    public UserCache getUserCache(String username) {
        String key = "UserCache:" + username;
        UserCache user = redisTemplate.opsForValue().get(key);
        log.info("User {} Cache find", username);
        return user;
    }

}
