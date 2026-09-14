package com.tuktuk.common.dto;

import java.math.BigDecimal;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class PaymentResponse {

    private Long id;
    private Long bookingId;
    private Long driverId;
    private Long passengerId;
    private BigDecimal amount;
    private String currency;
    private String status;

    /** Raw EMVCo/KHQR payload string (what the QR image encodes). */
    private String qrCode;

    /** "data:image/png;base64,..." — ready to render directly in an &lt;img&gt; tag. */
    private String qrImage;

    private String md5;
    private String transactionId;
    private Instant expiresAt;
    private Instant paidAt;
}
