package com.tuktuk.common.security;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "tuktuk.jwt")
public class JwtProperties {

    /** HMAC signing key; must be at least 256 bits (32 chars) long. */
    private String secret;

    private long expirationMs = 86_400_000L;

}
