package com.project.expensemanger.core.common.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.expensemanger.core.config.property.YamlPropertySourceFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@EnableConfigurationProperties(JwtProperties.class)
@PropertySource(value = "classpath:application.yml", factory = YamlPropertySourceFactory.class)
class JwtPropertiesTest {

    @Autowired
    private JwtProperties jwtProperties;

    @TestConfiguration
    static class Config {
        @Bean
        public JwtProperties jwtProperties() {
            return new JwtProperties();
        }
    }

    @Test
    @DisplayName("yml 설정 파일 읽어 오기")
    void jwtPropertiesCheck() {
        assertAll(
                () -> assertThat(jwtProperties.getTokenPrefix()).isEqualTo("Bearer "),
                () -> assertThat(jwtProperties.getAccessExpirationTime()).isNotZero(),
                () -> assertThat(jwtProperties.getRefreshExpirationTime()).isNotZero(),
                () -> assertThat(jwtProperties.getSecretKey()).isNotEmpty(),
                () -> assertThat(jwtProperties.getAuthoritiesKey()).isNotEmpty()
        );
    }
}