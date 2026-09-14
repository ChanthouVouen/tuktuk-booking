package com.tuktuk.core.service.serviceimpl;

import com.tuktuk.common.dto.PaymentResponse;
import com.tuktuk.common.exception.InvalidStateException;
import com.tuktuk.common.exception.ResourceNotFoundException;
import com.tuktuk.core.config.PaymentProperties;
import com.tuktuk.core.khqr.KhqrClient;
import com.tuktuk.core.mapper.PaymentMapper;
import com.tuktuk.core.service.PaymentService;
import com.tuktuk.domain.entity.Booking;
import com.tuktuk.domain.entity.Payment;
import com.tuktuk.domain.enums.BookingStatus;
import com.tuktuk.domain.enums.PaymentStatus;
import com.tuktuk.domain.repository.BookingRepository;
import com.tuktuk.domain.repository.PaymentRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private static final Set<String> SUCCESS_CODES = Set.of("0", "00");
    private static final Set<String> SUCCESS_STATUSES = Set.of("success", "successful", "paid", "completed");

    private final PaymentRepository paymentRepository;
    private final BookingRepository bookingRepository;
    private final KhqrClient khqrClient;
    private final PaymentProperties paymentProperties;

    @Override
    @Transactional
    public PaymentResponse generate(Long passengerId, Long bookingId) {
        Booking booking = bookingRepository.findByIdAndPassengerId(bookingId, passengerId)
                .orElseThrow(() -> new ResourceNotFoundException("Booking not found with id: " + bookingId));

        if (booking.getStatus() != BookingStatus.COMPLETED) {
            throw new InvalidStateException("Booking must be completed before it can be paid");
        }
        if (booking.getDriver() == null) {
            throw new InvalidStateException("Booking has no assigned driver");
        }
        if (booking.getDriver().getBakongAccountId() == null || booking.getDriver().getBakongAccountId().isBlank()) {
            throw new InvalidStateException("Driver has not linked a Bakong account to receive payments");
        }

        Payment payment = paymentRepository.findByBookingId(bookingId).orElse(null);
        if (payment != null) {
            if (payment.getStatus() == PaymentStatus.PAID) {
                throw new InvalidStateException("This booking has already been paid");
            }
            if (payment.getStatus() == PaymentStatus.PENDING && payment.getExpiresAt().isAfter(Instant.now())) {
                return PaymentMapper.toResponse(payment);
            }
        }

        KhqrClient.KhqrPayload khqr = khqrClient.buildPayload(booking);
        String qrImage = khqrClient.renderQrImageDataUri(khqr.qr());
        Instant expiresAt = Instant.now().plus(paymentProperties.expiryMinutes(), ChronoUnit.MINUTES);

        if (payment == null) {
            payment = Payment.builder()
                    .booking(booking)
                    .driver(booking.getDriver())
                    .passenger(booking.getPassenger())
                    .amount(booking.getPrice())
                    .currency(paymentProperties.defaultCurrency())
                    .build();
        }
        payment.setMd5(khqr.md5());
        payment.setQrPayload(khqr.qr());
        payment.setQrImageBase64(qrImage);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setExpiresAt(expiresAt);
        payment.setTransactionId(null);
        payment.setPaidAt(null);
        payment.setCheckAttempts(0);
        payment.setLastCheckedAt(null);

        payment = paymentRepository.save(payment);
        log.info("KHQR payment generated bookingId={} paymentId={} md5={} amount={} {}",
                bookingId, payment.getId(), payment.getMd5(), payment.getAmount(), payment.getCurrency());

        return PaymentMapper.toResponse(payment);
    }

    @Override
    @Transactional
    public PaymentResponse getStatusForPassenger(Long passengerId, Long bookingId) {
        Payment payment = findOwnedPayment(bookingId, passengerId, true);
        return PaymentMapper.toResponse(sync(payment));
    }

    @Override
    @Transactional
    public PaymentResponse getStatusForDriver(Long driverId, Long bookingId) {
        Payment payment = findOwnedPayment(bookingId, driverId, false);
        return PaymentMapper.toResponse(sync(payment));
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getQrImageForPassenger(Long passengerId, Long bookingId) {
        return decodePngDataUri(findOwnedPayment(bookingId, passengerId, true).getQrImageBase64());
    }

    @Override
    @Transactional(readOnly = true)
    public byte[] getQrImageForDriver(Long driverId, Long bookingId) {
        return decodePngDataUri(findOwnedPayment(bookingId, driverId, false).getQrImageBase64());
    }

    private static byte[] decodePngDataUri(String dataUri) {
        int comma = dataUri.indexOf(',');
        String base64 = comma >= 0 ? dataUri.substring(comma + 1) : dataUri;
        return Base64.getDecoder().decode(base64);
    }

    private Payment findOwnedPayment(Long bookingId, Long requesterId, boolean passengerSide) {
        Payment payment = paymentRepository.findByBookingId(bookingId)
                .orElseThrow(() -> new ResourceNotFoundException("No payment found for booking id: " + bookingId));

        Long ownerId = passengerSide ? payment.getPassenger().getId() : payment.getDriver().getId();
        if (!ownerId.equals(requesterId)) {
            throw new ResourceNotFoundException("No payment found for booking id: " + bookingId);
        }
        return payment;
    }

    private Payment sync(Payment payment) {
        if (payment.getStatus() == PaymentStatus.PAID || payment.getStatus() == PaymentStatus.EXPIRED) {
            return payment;
        }
        if (payment.getExpiresAt().isBefore(Instant.now())) {
            payment.setStatus(PaymentStatus.EXPIRED);
            return paymentRepository.save(payment);
        }

        Map<String, Object> result = khqrClient.checkTransactionByMd5(payment.getMd5());
        payment.setCheckAttempts(payment.getCheckAttempts() + 1);
        payment.setLastCheckedAt(Instant.now());

        if (result.containsKey("error")) {
            log.warn("Bakong gateway call failed paymentId={} md5={} error={} — check tuktuk.bakong.token "
                            + "hasn't expired (Bakong tokens are short-lived) and tuktuk.bakong.api-url is reachable",
                    payment.getId(), payment.getMd5(), result.get("error"));
        } else {
            log.info("Payment sync check paymentId={} md5={} bakongResponse={}", payment.getId(), payment.getMd5(), result);
        }

        if (isGatewaySuccess(result)) {
            Map<String, Object> txInfo = khqrClient.getTransactionByMd5(payment.getMd5());
            payment.setTransactionId(extractTransactionId(result, txInfo));
            payment.setStatus(PaymentStatus.PAID);
            payment.setPaidAt(Instant.now());
        }

        return paymentRepository.save(payment);
    }

    private boolean isGatewaySuccess(Map<String, Object> result) {
        Object responseCode = firstNonNull(
                result.get("responseCode"),
                result.get("response_code"),
                nestedGet(result, "status", "code"),
                result.get("code")
        );
        if (responseCode != null && SUCCESS_CODES.contains(String.valueOf(responseCode).trim())) {
            return true;
        }

        Object statusValue = firstNonNull(
                result.get("transactionStatus"),
                result.get("transaction_status"),
                nestedGet(result, "status", "message"),
                result.get("status")
        );
        String status = statusValue == null ? "" : String.valueOf(statusValue).trim().toLowerCase(Locale.ROOT);
        return SUCCESS_STATUSES.contains(status);
    }

    private String extractTransactionId(Map<String, Object> result, Map<String, Object> txInfo) {
        Object[] candidates = {
                nestedGet(txInfo, "data", "hash"),
                nestedGet(txInfo, "data", "transactionId"),
                nestedGet(txInfo, "data", "transaction_id"),
                nestedGet(result, "data", "hash"),
                nestedGet(result, "data", "transactionId"),
                nestedGet(result, "data", "transaction_id"),
        };
        for (Object candidate : candidates) {
            String value = candidate == null ? "" : String.valueOf(candidate).trim();
            if (!value.isEmpty()) {
                return value;
            }
        }
        return null;
    }

    private static Object firstNonNull(Object... values) {
        for (Object v : values) {
            if (v != null) {
                return v;
            }
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    private static Object nestedGet(Map<String, Object> map, String key, String subKey) {
        if (map == null) {
            return null;
        }
        Object nested = map.get(key);
        return nested instanceof Map<?, ?> nestedMap ? ((Map<String, Object>) nestedMap).get(subKey) : null;
    }

}
