package com.ThreeK_Project.api_server.domain.user.repository;

import com.ThreeK_Project.api_server.domain.user.entity.User;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Repository;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserCacheRepository {

    private final String USER_CACHE_KEY_PREFIX = "UserCache:";

    private final RedisTemplate<String, User> redisTemplate;

    public void setUserCache(String username, User user) {
        String key = USER_CACHE_KEY_PREFIX + username;
        redisTemplate.opsForValue().set(key, user, Duration.ofHours(1));
        log.info("User {} Cache Saved", username);
    }

    public User getUserCache(String username) {
        String key = USER_CACHE_KEY_PREFIX + username;
        User user = redisTemplate.opsForValue().get(key);
        log.info("User {} Cache find", username);
        return user;
    }

    public void deleteUserCache(String username) {
        String key = USER_CACHE_KEY_PREFIX + username;
        redisTemplate.delete(key);
        log.info("User {} Cache Deleted", username);
    }

}
