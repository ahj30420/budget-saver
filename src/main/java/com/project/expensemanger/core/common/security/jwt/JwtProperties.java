package com.project.expensemanger.core.common.security.jwt;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties("spring.jwt")
public class JwtProperties {
    public int accessExpirationTime;
    public int refreshExpirationTime;
    public String authoritiesKey;
    public String tokenPrefix;
    public String secretKey;
}
