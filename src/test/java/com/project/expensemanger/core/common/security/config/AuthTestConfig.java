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
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthTestConfig {

    @Bean
    public JwtProperties jwtProperties() { return new JwtProperties(); }

    @Bean
    public JwtProvider jwtProvider() { return new JwtProvider(jwtProperties()); }

    @Bean
    public ResponseUtil responseUtil() { return new ResponseUtil(new ObjectMapper()); }

    @Bean
    public CustomAuthenticationFailureHandler customAuthenticationFailureHandler() {
        return new CustomAuthenticationFailureHandler(responseUtil());
    }

    @Bean
    public CustomAccessDeniedHandler customAccessDeniedHandler() {
        return new CustomAccessDeniedHandler(responseUtil());
    }

    @Bean
    public CustomAuthenticationEntryPoint customAuthenticationEntryPoint() {
        return new CustomAuthenticationEntryPoint(responseUtil());
    }

    @Bean
    public CustomAuthenticationSuccessHandler customAuthenticationSuccessHandler() {
        return new CustomAuthenticationSuccessHandler(responseUtil(), jwtProvider(), jwtProperties());
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(responseUtil());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserMock userMock() {
        return new UserMock(passwordEncoder());
    }

    @Bean
    public UserUseCase userUseCase() { return Mockito.mock(UserUseCase.class); }

    @Bean
    public UserMapper userMapper() { return new UserMapper(); }
}
