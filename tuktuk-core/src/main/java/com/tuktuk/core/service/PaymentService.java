package com.tuktuk.core.service;

import com.tuktuk.common.dto.PaymentResponse;

public interface PaymentService {

    /** Generates (or, if still valid, reuses) the KHQR payment QR for a completed booking. */
    PaymentResponse generate(Long passengerId, Long bookingId);

    /** Re-checks the Bakong gateway and returns the current payment status, for the paying passenger. */
    PaymentResponse getStatusForPassenger(Long passengerId, Long bookingId);

    /** Re-checks the Bakong gateway and returns the current payment status, for the receiving driver. */
    PaymentResponse getStatusForDriver(Long driverId, Long bookingId);

    /** Raw PNG bytes of the already-generated KHQR code, for the paying passenger. */
    byte[] getQrImageForPassenger(Long passengerId, Long bookingId);

    /** Raw PNG bytes of the already-generated KHQR code, for the receiving driver. */
    byte[] getQrImageForDriver(Long driverId, Long bookingId);

}
