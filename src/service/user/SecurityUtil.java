package service.user;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

public class SecurityUtil {
    private static final Map<String, String> activeOtpStore = new HashMap<>();
    private static final Random random = new Random();

    // SHA-256 One-Way Password Hashing
    public static String hashPassword(String rawPassword) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodedhash = digest.digest(rawPassword.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : encodedhash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    // Simulates generating a 6-digit OTP for SMS/Email
    public static String generateAndSendOtp(String contactTarget, String purpose) {
        String otp = String.format("%06d", random.nextInt(999999));
        activeOtpStore.put(contactTarget, otp);
        System.out.printf("[OTP SERVICE] Sent OTP [%s] to %s for %s.%n", otp, contactTarget, purpose);
        return otp;
    }

    // Verifies submitted OTP against stored value
    public static boolean verifyOtp(String contactTarget, String submittedOtp) {
        String storedOtp = activeOtpStore.get(contactTarget);
        if (storedOtp != null && storedOtp.equals(submittedOtp)) {
            activeOtpStore.remove(contactTarget); // Invalidate after use
            return true;
        }
        return false;
    }

    public static String generateRememberMeToken() {
        return UUID.randomUUID().toString() + "-" + System.currentTimeMillis();
    }
}