package com.tuktuk.core.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Credentials for the National Bank of Cambodia's Bakong Open API, used to verify
 * KHQR payments (https://api-bakong.nbc.gov.kh). Get a renewable token from the
 * Bakong Developer Portal and set it via the BAKONG_TOKEN env var — never commit
 * a real value. The payee account/name are NOT here: each KHQR is generated
 * against the receiving driver's own {@code bakongAccountId} (see {@link com.tuktuk.domain.entity.Driver}),
 * so money goes straight to that driver rather than a shared platform account.
 */
@ConfigurationProperties(prefix = "tuktuk.bakong")
public record BakongProperties(String apiUrl, String token, Merchant merchant) {

    /** Fields that apply platform-wide regardless of which driver is being paid. */
    public record Merchant(String city, String acquiringBank) {
    }
}
