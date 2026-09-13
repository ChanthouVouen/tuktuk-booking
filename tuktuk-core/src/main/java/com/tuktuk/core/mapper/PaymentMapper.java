package com.tuktuk.core.mapper;

import com.tuktuk.common.dto.PaymentResponse;
import com.tuktuk.domain.entity.Payment;

public final class PaymentMapper {

    private PaymentMapper() {
    }

    public static PaymentResponse toResponse(Payment payment) {
        return PaymentResponse.builder()
                .id(payment.getId())
                .bookingId(payment.getBooking().getId())
                .driverId(payment.getDriver().getId())
                .passengerId(payment.getPassenger().getId())
                .amount(payment.getAmount())
                .currency(payment.getCurrency())
                .status(payment.getStatus().name())
                .qrCode(payment.getQrPayload())
                .qrImage(payment.getQrImageBase64())
                .md5(payment.getMd5())
                .transactionId(payment.getTransactionId())
                .expiresAt(payment.getExpiresAt())
                .paidAt(payment.getPaidAt())
                .build();
    }

}
