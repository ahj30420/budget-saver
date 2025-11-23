package com.project.expensemanger.core.common.util;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CookieUtils {

    @Value("${spring.jwt.refresh-expiration-time}")
    private int refreshExpirationTime;

    public Cookie createTokenCookie(String token) {
        Cookie cookie = new Cookie("refresh_token", token);
        cookie.setMaxAge(refreshExpirationTime);
        cookie.setHttpOnly(true);
        cookie.setSecure(true);
        cookie.setPath("/");
        return cookie;
    }
}
