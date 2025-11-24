package com.project.expensemanger.core.common.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.project.expensemanger.core.common.security.handler.CustomAccessDeniedHandler;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationEntryPoint;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationFailureHandler;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationSuccessHandler;
import com.project.expensemanger.core.common.security.handler.CustomLogoutSuccessHandler;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.util.CookieUtils;
import com.project.expensemanger.core.common.util.ResponseUtil;
import com.project.expensemanger.manager.adaptor.in.api.mapper.UserMapper;
import com.project.expensemanger.manager.adaptor.out.AuthPersistenceAdapter;
import com.project.expensemanger.manager.adaptor.out.redis.RedisRepository;
import com.project.expensemanger.manager.application.mock.UserMock;
import com.project.expensemanger.manager.application.port.in.AuthUseCase;
import com.project.expensemanger.manager.application.port.in.UserUseCase;
import com.project.expensemanger.manager.application.port.out.AuthPort;
import com.project.expensemanger.manager.application.port.out.UserPort;
import com.project.expensemanger.manager.application.service.AuthService;
import com.project.expensemanger.manager.application.service.UserService;
import org.mockito.Mockito;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class AuthTestConfig {

    @Bean
    public JwtProperties jwtProperties() {
        return new JwtProperties();
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(jwtProperties());
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        return mapper;
    }

    @Bean
    public CookieUtils cookieUtils() {
        return new CookieUtils();
    }

    @Bean
    public ResponseUtil responseUtil() {
        return new ResponseUtil(objectMapper(), cookieUtils(), jwtProperties());
    }

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
        return new CustomAuthenticationSuccessHandler(responseUtil(), jwtProvider(), authUseCase());
    }

    @Bean
    public CustomLogoutSuccessHandler customLogoutSuccessHandler() {
        return new CustomLogoutSuccessHandler(responseUtil(), cookieUtils(), jwtProvider(), authUseCase());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserService(userPort(), passwordEncoder());
    }

    @Bean
    public AuthPort authPort() {
        return Mockito.mock(AuthPort.class);
    }

    @Bean
    public UserPort userPort() {
        return Mockito.mock(UserPort.class);
    }

    @Bean
    public AuthUseCase authUseCase() {
        return new AuthService(
                authPort(),
                userPort(),
                cookieUtils(),
                jwtProvider(),
                responseUtil()
        );
    }

    @Bean
    public UserMock userMock() {
        return new UserMock(passwordEncoder());
    }

    @Bean
    public UserUseCase userUseCase() {
        return Mockito.mock(UserUseCase.class);
    }

    @Bean
    public UserMapper userMapper() {
        return new UserMapper();
    }
}
