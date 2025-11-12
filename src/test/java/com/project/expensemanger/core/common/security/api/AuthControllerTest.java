package com.project.expensemanger.core.common.security.api;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.exception.errorcode.AuthErrorCode;
import com.project.expensemanger.core.common.security.dto.LoginRequest;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.manager.application.mock.UserMock;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.domain.User.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthControllerTest {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    JwtProperties jwtProperties;

    @MockitoBean
    UserPort userPort;

    @Autowired
    UserMock userMock;

    @Test
    @DisplayName("로그인 테스트 : 성공")
    void login_success_test() throws Exception {
        // given
        LoginRequest loginDto = userMock.loginMock();
        User mockUser = userMock.domainMock();
        String content = objectMapper.writeValueAsString(loginDto);

        given(userPort.findByEmail(anyString())).willReturn(mockUser);

        // when & then
        String authorizationHeader = mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/login")
                                .content(content)
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getHeader(HttpHeaders.AUTHORIZATION);

        assertThat(authorizationHeader).startsWith(jwtProperties.getTokenPrefix());
    }

    @Test
    @DisplayName("로그인 테스트 : 실패")
    void login_failure_test() throws Exception {
        // given
        LoginRequest loginRequest = userMock.wrongLoginMock();
        User mockUser = userMock.domainMock();
        String content = objectMapper.writeValueAsString(loginRequest);

        given(userPort.findByEmail(anyString())).willReturn(mockUser);

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/login")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON));

        // then
        perform.andExpect(status().isUnauthorized());
    }

    @Test
    @DisplayName("토큰 시간 초과 : 만료")
    void token_expire_test() throws Exception {
        // given
        String accessToken = generateAccessToken("test@naver.com", 1L, List.of((GrantedAuthority) () -> "ROLE_USER"),
                -1, "");

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/category")
                        .header(HttpHeaders.AUTHORIZATION, jwtProperties.getTokenPrefix() + accessToken)
                        .characterEncoding("UTF-8"));

        // then
        perform.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.body.message")
                                .value(AuthErrorCode.ACCESS_TOKEN_EXPIRED.getMessage()));
    }

    @Test
    @DisplayName("토큰 시그니처 에러")
    void token_signature_test() throws Exception {
        // given
        String accessToken = generateAccessToken("test@naver.com", 1L, List.of((GrantedAuthority) () -> "ROLE_USER"),
                20000, "error");

        // when
        ResultActions perform = mockMvc.perform(
                MockMvcRequestBuilders.post("/api/category")
                        .header(HttpHeaders.AUTHORIZATION, jwtProperties.getTokenPrefix() + accessToken)
                        .characterEncoding("UTF-8"));

        // then
        perform.andExpect(MockMvcResultMatchers.status().isUnauthorized())
                .andExpect(
                        MockMvcResultMatchers.jsonPath("$.body.message")
                                .value(AuthErrorCode.INVALID_SIGNATURE_ACCESS_TOKEN.getMessage()));

    }

    private String generateAccessToken(String subject, Long id,
                                       Collection<? extends GrantedAuthority> authorities,
                                       int expire, String key) {
        long now = new Date().getTime();

        List<String> roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        return Jwts.builder()
                .subject(subject)
                .claim("id", id)
                .claim(jwtProperties.authoritiesKey, roles)
                .issuedAt(new Date(now))
                .expiration(new Date(now + expire))
                .signWith(getEncodedKey(key))
                .compact();
    }

    private SecretKey getEncodedKey(String parameter) {
        byte[] byteSecretKey = Decoders.BASE64.decode(jwtProperties.getSecretKey() + parameter);
        return Keys.hmacShaKeyFor(byteSecretKey);
    }
}
