package com.project.expensemanger.core.common.security.jwt;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.project.expensemanger.manager.domain.User.UserRole;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collections;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

class JwtProviderTest {

    JwtProvider sut;

    JwtProperties jwtProperties;

    @BeforeEach
    void setUp() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecretKey("YmFzZTY0ZW5jb2RlZHNlY3JldA==asdfzxcvawsdfqwersadfzxcvasdf");
        jwtProperties.setAccessExpirationTime(1000 * 60);
        jwtProperties.setRefreshExpirationTime(1000 * 60 * 60);
        jwtProperties.setAuthoritiesKey("roles");

        sut = new JwtProvider(jwtProperties);
    }

    @Test
    @DisplayName("AccessToken 구현 테스트")
    void generate_access_token_test() {
        // given
        String accessToken = sut.generateAccessToken(
                "test@naver.com",
                1L,
                Collections.singleton(new SimpleGrantedAuthority(UserRole.USER.name()))
        );

        // when
        Claims payload = Jwts.parser()
                .verifyWith(getSecretKey())
                .build()
                .parseSignedClaims(accessToken)
                .getPayload();

        // then
        assertAll(
                () -> assertThat(payload.getSubject()).isEqualTo("test@naver.com"),
                () -> assertThat(payload.get("id", Long.class)).isEqualTo(1L)
        );
    }

    private SecretKey getSecretKey() {
        byte[] byteSecretKey = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(byteSecretKey);
    }
}