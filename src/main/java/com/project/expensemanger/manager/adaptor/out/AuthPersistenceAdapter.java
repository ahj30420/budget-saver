package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.cache.RedisKeyGenerator;
import com.project.expensemanger.manager.application.port.out.AuthPort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthPort {

    @Value("${spring.jwt.refresh-expiration-time}")
    private int refreshExpirationTime;

    private final StringRedisTemplate stringRedisTemplate;

    @Override
    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = RedisKeyGenerator.getRefreshTokenKey(userId);
        stringRedisTemplate.opsForValue()
                .set(key, refreshToken, refreshExpirationTime, TimeUnit.MILLISECONDS);
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        stringRedisTemplate.delete(RedisKeyGenerator.getRefreshTokenKey(userId));
    }

    @Override
    public String getRefreshToken(Long userId) {
        return stringRedisTemplate.opsForValue().get(RedisKeyGenerator.getRefreshTokenKey(userId));
    }
}
