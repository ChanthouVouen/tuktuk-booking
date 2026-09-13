package com.tuktuk.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "tuktuk.payment")
public record PaymentProperties(int expiryMinutes, String defaultCurrency) {
}
