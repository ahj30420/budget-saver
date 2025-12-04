package com.project.expensemanger.manager.adaptor.out.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;

@Testcontainers
@DataRedisTest(excludeAutoConfiguration = {
        org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@Import({RedisRepository.class, ObjectMapper.class})
class RedisRepositoryTest {

    private static final String REDIS_IMAGE = "redis:7.0.8-alpine";
    private static final int REDIS_PORT = 6379;
    private static final GenericContainer redis;

    static {
        redis = new GenericContainer(REDIS_IMAGE)
                .withExposedPorts(REDIS_PORT)
                .withReuse(true);
        redis.start();
    }

    @DynamicPropertySource
    private static void registerRedisProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", redis::getHost);
        registry.add("spring.data.redis.port", () -> redis.getMappedPort(REDIS_PORT)
                .toString());
    }

    @Autowired
    RedisRepository redisRepository;

    @Autowired
    ObjectMapper objectMapper;

    @DynamicPropertySource
    static void redisProperties(DynamicPropertyRegistry registry) {
        redis.start();
        registry.add("spring.redis.host", redis::getHost);
        registry.add("spring.redis.port", redis::getFirstMappedPort);
    }

    @Test
    @DisplayName("redis 저장 테스트")
    void save_test() throws Exception {
        // given
        String key = "test";
        RecommendedBudgetResult data = new RecommendedBudgetResult(1L, "test", 10000L);
        String value = objectMapper.writeValueAsString(data);

        // when
        redisRepository.save(key, value, 10, TimeUnit.SECONDS);
        RecommendedBudgetResult result = objectMapper.readValue(value, RecommendedBudgetResult.class);

        // then
        assertNotNull(result);
        assertThat(result.getCategoryId()).isEqualTo(1L);
        assertThat(result.getCategoryName()).isEqualTo("test");
        assertThat(result.getRecommendedAmount()).isEqualTo(10000L);
    }

    @Test
    @DisplayName("redis 삭제 테스트")
    void delete_test() throws Exception {
        // given
        String key = "test";
        String value = "test";

        // when
        redisRepository.save(key, value, 10, TimeUnit.SECONDS);
        redisRepository.delete(key);
        String result = redisRepository.findByKey(key);

        // then
        assertNull(result);
    }
}