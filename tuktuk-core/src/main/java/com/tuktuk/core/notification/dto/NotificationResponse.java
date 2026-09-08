package com.tuktuk.core.notification.dto;

import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class NotificationResponse {

    private Long id;
    private String message;
    private boolean read;
    private Instant createdAt;
}