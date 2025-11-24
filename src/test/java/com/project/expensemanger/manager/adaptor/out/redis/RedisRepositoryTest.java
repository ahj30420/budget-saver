package com.project.expensemanger.manager.adaptor.out.redis;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.manager.domain.budget.recommendation.vo.RecommendedBudgetResult;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.redis.DataRedisTest;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ExtendWith(SpringExtension.class)
@DataRedisTest(excludeAutoConfiguration = {
    org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration.class
})
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Import({RedisRepository.class, ObjectMapper.class})
class RedisRepositoryTest {

    private static final int PORT = 6379;

    @Container
    static GenericContainer redis =
            new GenericContainer<>(DockerImageName.parse("redis")).withExposedPorts(PORT);

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