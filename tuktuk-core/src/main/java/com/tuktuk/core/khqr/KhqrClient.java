package com.tuktuk.core.khqr;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.tuktuk.core.config.BakongProperties;
import com.tuktuk.core.config.PaymentProperties;
import com.tuktuk.domain.entity.Booking;
import com.tuktuk.domain.entity.Driver;
import java.io.ByteArrayOutputStream;
import java.io.IOException; 
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

/**
 * Builds Bakong KHQR (EMVCo-based) payment QR codes and calls the Bakong Open API
 * to verify them. https://api-bakong.nbc.gov.kh is the National Bank of Cambodia's
 * real payment gateway — every driver/passenger/booking scanning this code is paid
 * for through that live rail, not a mock.
 */
@Component
public class KhqrClient {

    /** Merchant Category Code for "Transportation Services — Taxicabs and Limousines". */
    private static final String MCC_TRANSPORTATION = "4121";
    private static final String COUNTRY_CODE = "KH";

    private final RestTemplate restTemplate;
    private final BakongProperties bakongProperties;
    private final PaymentProperties paymentProperties;

    public KhqrClient(RestTemplate bakongRestTemplate, BakongProperties bakongProperties,
                       PaymentProperties paymentProperties) {
        this.restTemplate = bakongRestTemplate;
        this.bakongProperties = bakongProperties;
        this.paymentProperties = paymentProperties;
    }

    public record KhqrPayload(String qr, String md5) {
    }

    /**
     * Builds a dynamic KHQR payload for one booking, payable directly into the assigned
     * driver's own Bakong account. The amount (tag 54) and currency (tag 53) are the real
     * payment terms; the booking id, driver id and passenger id are carried in the
     * Additional Data Field Template (tag 62) so the payment can be reconciled back to
     * this exact ride once Bakong reports it as paid.
     */
    public KhqrPayload buildPayload(Booking booking) {
        BakongProperties.Merchant merchant = bakongProperties.merchant();
        Driver driver = booking.getDriver();
        String driverBakongId = driver.getBakongAccountId();
        String displayName = notBlank(driver.getMerchantName()) ? driver.getMerchantName() : driver.getFullName();
        BigDecimal amount = booking.getPrice().setScale(2, RoundingMode.HALF_UP);
        String currency = paymentProperties.defaultCurrency();

        long nowMs = System.currentTimeMillis();
        long expiryMs = nowMs + paymentProperties.expiryMinutes() * 60_000L;

        StringBuilder qr = new StringBuilder();
        qr.append(tlv("00", "01"));  // Payload Format Indicator
        qr.append(tlv("01", "12"));  // Point of Initiation Method: 12 = dynamic (has an amount)

        StringBuilder merchantAccount = new StringBuilder();
        merchantAccount.append(tlv("00", driverBakongId));
        if (notBlank(merchant.acquiringBank())) {
            merchantAccount.append(tlv("02", merchant.acquiringBank()));
        }
        qr.append(tlv("29", merchantAccount.toString()));

        qr.append(tlv("52", MCC_TRANSPORTATION));
        qr.append(tlv("53", "KHR".equalsIgnoreCase(currency) ? "116" : "840"));
        qr.append(tlv("54", amount.toPlainString()));
        qr.append(tlv("58", COUNTRY_CODE));
        qr.append(tlv("59", truncate(displayName, 25)));
        qr.append(tlv("60", truncate(merchant.city(), 15)));

        StringBuilder additionalData = new StringBuilder();
        additionalData.append(tlv("01", "BK" + booking.getId()));
        if (notBlank(driver.getPhoneNumber())) {
            additionalData.append(tlv("02", driver.getPhoneNumber()));
        }
        additionalData.append(tlv("03", "DRV" + driver.getId()));
        additionalData.append(tlv("07", "PSG" + booking.getPassenger().getId()));
        qr.append(tlv("62", additionalData.toString()));

        StringBuilder timestamp = new StringBuilder();
        timestamp.append(tlv("00", String.valueOf(nowMs)));
        timestamp.append(tlv("01", String.valueOf(expiryMs)));
        qr.append(tlv("99", timestamp.toString()));

        // CRC (tag 63) is computed over everything above plus this tag+length, but not the CRC value itself.
        qr.append("6304");
        String finalQr = qr + String.format("%04X", crc16(qr.toString()));

        return new KhqrPayload(finalQr, md5Hex(finalQr));
    }

    /** Renders the KHQR payload as a scannable PNG, returned as a data URI. */
    public String renderQrImageDataUri(String payload) {
        try {
            Map<EncodeHintType, Object> hints = new EnumMap<>(EncodeHintType.class);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
            hints.put(EncodeHintType.MARGIN, 1);

            BitMatrix matrix = new QRCodeWriter().encode(payload, BarcodeFormat.QR_CODE, 400, 400, hints);
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", out);

            return "data:image/png;base64," + Base64.getEncoder().encodeToString(out.toByteArray());
        } catch (WriterException | IOException e) {
            throw new IllegalStateException("Failed to render KHQR image", e);
        }
    }

    /** Calls Bakong's check_transaction_by_md5 — the real "has this been paid?" check. */
    public Map<String, Object> checkTransactionByMd5(String md5) {
        return post("/v1/check_transaction_by_md5", Map.of("md5", md5));
    }

    /** Calls Bakong's get_transaction_by_md5 for the full transaction detail (hash, timestamps, etc). */
    public Map<String, Object> getTransactionByMd5(String md5) {
        return post("/v1/get_transaction_by_md5", Map.of("md5", md5));
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private Map<String, Object> post(String path, Map<String, Object> body) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(bakongProperties.token());
            headers.setContentType(MediaType.APPLICATION_JSON);

            ResponseEntity<Map> response = restTemplate.exchange(
                    bakongProperties.apiUrl() + path,
                    HttpMethod.POST,
                    new HttpEntity<>(body, headers),
                    Map.class);

            Map<String, Object> json = response.getBody();
            return json != null ? json : Map.of();
        } catch (RestClientException e) {
            Map<String, Object> error = new LinkedHashMap<>();
            error.put("error", e.getMessage());
            return error;
        }
    }

    private static String tlv(String tag, String value) {
        return tag + String.format("%02d", value.length()) + value;
    }

    /** CRC16-CCITT (FALSE) — the checksum algorithm mandated by the EMVCo QR spec. */
    private static int crc16(String data) {
        int crc = 0xFFFF;
        int poly = 0x1021;
        for (byte b : data.getBytes(StandardCharsets.US_ASCII)) {
            crc ^= (b & 0xFF) << 8;
            for (int j = 0; j < 8; j++) {
                crc = ((crc & 0x8000) != 0) ? ((crc << 1) ^ poly) & 0xFFFF : (crc << 1) & 0xFFFF;
            }
        }
        return crc;
    }

    private static boolean notBlank(String s) {
        return s != null && !s.isBlank();
    }

    private static String truncate(String s, int max) {
        if (s == null) {
            return "";
        }
        return s.length() > max ? s.substring(0, max) : s;
    }

    private static String md5Hex(String input) {
        try {
            byte[] digest = MessageDigest.getInstance("MD5").digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalStateException(e);
        }
    }

}
