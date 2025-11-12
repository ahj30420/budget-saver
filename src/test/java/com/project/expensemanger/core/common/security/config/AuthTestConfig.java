package com.project.expensemanger.core.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.security.handler.CustomAccessDeniedHandler;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationEntryPoint;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationFailureHandler;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationSuccessHandler;
import com.project.expensemanger.core.common.security.handler.CustomLogoutSuccessHandler;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.util.ResponseUtil;
import com.project.expensemanger.manager.adaptor.in.api.mapper.UserMapper;
import com.project.expensemanger.manager.application.mock.UserMock;
import com.project.expensemanger.manager.application.port.in.UserUseCase;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthTestConfig {

    @Bean
    public JwtProperties jwtProperties() { return new JwtProperties(); }

    @Bean
    public JwtProvider jwtProvider() { return new JwtProvider(jwtProperties()); }

    @Bean
    public ResponseUtil responseUtil(ObjectMapper objectMapper) { return new ResponseUtil(objectMapper); }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler(ObjectMapper objectMapper) {
        return new CustomAuthenticationFailureHandler(responseUtil(objectMapper));
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler(ObjectMapper objectMapper) {
        return new CustomAccessDeniedHandler(responseUtil(objectMapper));
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint(ObjectMapper objectMapper) {
        return new CustomAuthenticationEntryPoint(responseUtil(objectMapper));
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler(ObjectMapper objectMapper) {
        return new CustomAuthenticationSuccessHandler(responseUtil(objectMapper), jwtProvider(), jwtProperties());
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler(ObjectMapper objectMapper) {
        return new CustomLogoutSuccessHandler(responseUtil(objectMapper));
    }

    @Bean
    public UserMock userMock() {
        return new UserMock(new BCryptPasswordEncoder());
    }

    @Bean
    public UserUseCase userUseCase() { return Mockito.mock(UserUseCase.class); }

    @Bean
    public UserMapper userMapper() { return new UserMapper(); }
}
