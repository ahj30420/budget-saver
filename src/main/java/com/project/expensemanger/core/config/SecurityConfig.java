package com.project.expensemanger.core.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.project.expensemanger.core.common.security.filter.JwtAuthenticationFilter;
import com.project.expensemanger.core.common.security.filter.JwtVerificationFilter;
import com.project.expensemanger.core.common.security.handler.CustomAccessDeniedHandler;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationEntryPoint;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationFailureHandler;
import com.project.expensemanger.core.common.security.handler.CustomAuthenticationSuccessHandler;
import com.project.expensemanger.core.common.security.handler.CustomLogoutSuccessHandler;
import com.project.expensemanger.core.common.security.jwt.JwtProperties;
import com.project.expensemanger.core.common.security.jwt.JwtProvider;
import com.project.expensemanger.core.common.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomAuthenticationFailureHandler failureHandler;
    private final CustomAccessDeniedHandler accessDeniedHandler;
    private final CustomAuthenticationEntryPoint authenticationEntryPoint;
    private final CustomLogoutSuccessHandler logoutSuccessHandler;
    private final ObjectMapper objectMapper;

    private static final String[] PUBLIC_ENDPOINTS = {
            "/api/signup",
            "/api/login",
            "/swagger-ui/**",
            "/swagger-ui/index.html",
            "/swagger-ui.html",
            "/v3/api-docs/**",
            "/swagger-resources/**",
            "/webjars/**",
    };

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class).build();
    }

        @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   AuthenticationManager authenticationManager,
                                                   CustomAuthenticationSuccessHandler successHandler,
                                                   JwtProperties jwtProperties,
                                                   JwtProvider jwtProvider,
                                                   ResponseUtil responseUtil) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .httpBasic(AbstractHttpConfigurer::disable)
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(PUBLIC_ENDPOINTS).permitAll()
                        .anyRequest().authenticated());

        http
                .addFilterBefore(new JwtVerificationFilter(responseUtil, jwtProperties, jwtProvider),
                        JwtAuthenticationFilter.class)
                .addFilterAt(jwtAuthenticationFilter(authenticationManager, successHandler),
                        UsernamePasswordAuthenticationFilter.class)
                .logout(logout -> logout.logoutSuccessHandler(logoutSuccessHandler).logoutUrl("/api/logout"))
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(authenticationEntryPoint)
                        .accessDeniedHandler(accessDeniedHandler));

        return http.build();
    }

    private JwtAuthenticationFilter jwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                                           CustomAuthenticationSuccessHandler successHandler) {
        JwtAuthenticationFilter filter = new JwtAuthenticationFilter(objectMapper);
        filter.setFilterProcessesUrl("/api/login");
        filter.setAuthenticationManager(authenticationManager);
        filter.setAuthenticationSuccessHandler(successHandler);
        filter.setAuthenticationFailureHandler(failureHandler);
        return filter;
    }
}
