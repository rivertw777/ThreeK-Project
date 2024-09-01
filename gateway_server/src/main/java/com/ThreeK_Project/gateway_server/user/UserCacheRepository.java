package com.ThreeK_Project.gateway_server.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final RedisTemplate<String, User> redisTemplate;

    public User getUserCache(String username) {
        String key = "UserCache:" + username;
        User user = redisTemplate.opsForValue().get(key);
        log.info("User {} Cache find", username);
        return user;
    }

}
