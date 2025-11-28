package com.project.expensemanger.manager.adaptor.out;

import com.project.expensemanger.core.common.cache.RedisKeyGenerator;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.manager.adaptor.out.redis.RedisRepository;
import com.project.expensemanger.manager.application.port.out.AuthPort;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AuthPersistenceAdapter implements AuthPort {

    private final JwtProperties jwtProperties;
    private final RedisRepository redisRepository;

    @Override
    public void saveRefreshToken(Long userId, String refreshToken) {
        String key = RedisKeyGenerator.getRefreshTokenKey(userId);
        redisRepository.save(key, refreshToken, jwtProperties.refreshExpirationTime, TimeUnit.SECONDS);
    }

    @Override
    public void deleteRefreshToken(Long userId) {
        String key = RedisKeyGenerator.getRefreshTokenKey(userId);
        redisRepository.delete(key);
    }

    @Override
    public String getRefreshToken(Long userId) {
        String key = RedisKeyGenerator.getRefreshTokenKey(userId);
        return redisRepository.findByKey(key);
    }
}
