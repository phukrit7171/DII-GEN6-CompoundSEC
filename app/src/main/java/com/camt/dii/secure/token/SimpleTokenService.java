package com.camt.dii.secure.token;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Base64;

public class SimpleTokenService implements TokenService {
    private static final String SECRET_KEY = "secretKey123";
    private static final int TIME_WINDOW_MINUTES = 5;

    @Override
    public String generateToken(String cardId) {
        LocalDateTime now = LocalDateTime.now();
        String tokenData = cardId + "|" + now.toString() + "|" + SECRET_KEY;
        byte[] hash = hashData(tokenData.getBytes());
        return Base64.getEncoder().encodeToString(hash) + "|" + now.toString();
    }

    @Override
    public boolean isValidToken(String cardId, String token) {
        String[] parts = token.split("\\|");
        if (parts.length != 2) return false;

        String tokenHash = parts[0];
        String tokenTimeStr = parts[1];

        LocalDateTime tokenTime;
        try {
            tokenTime = LocalDateTime.parse(tokenTimeStr);
        } catch (Exception e) {
            return false;
        }

        LocalDateTime now = LocalDateTime.now();
        if (tokenTime.isBefore(now.minusMinutes(TIME_WINDOW_MINUTES)) 
            || tokenTime.isAfter(now)) {
            return false;
        }

        String validationData = cardId + "|" + tokenTimeStr + "|" + SECRET_KEY;
        byte[] expectedHash = hashData(validationData.getBytes());
        return Arrays.equals(expectedHash, Base64.getDecoder().decode(tokenHash));
    }

    private byte[] hashData(byte[] data) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("SHA-256");
            return md.digest(data);
        } catch (Exception e) {
            throw new RuntimeException("Hashing failed", e);
        }
    }
}