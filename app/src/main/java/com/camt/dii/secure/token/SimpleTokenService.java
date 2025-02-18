package com.camt.dii.secure.token;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class SimpleTokenService implements TokenService {
    private final String secretKey;
    private final Map<String, TokenData> tokenStore = new HashMap<>();

    public SimpleTokenService(String secretKey) {
        this.secretKey = secretKey;
    }

    @Override
    public String generateToken(String cardId) {
        String tokenId = UUID.randomUUID().toString();
        String tokenValue = hash(cardId + secretKey + tokenId + LocalDateTime.now());
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(5);
        tokenStore.put(cardId, new TokenData(tokenValue, expiryTime, tokenId));
        return tokenValue;
    }

    @Override
    public boolean isValidToken(String cardId, String token) {
        TokenData tokenData = tokenStore.get(cardId);
        if (tokenData == null || !tokenData.tokenValue().equals(token)) {
            return false;
        }
        return LocalDateTime.now().isBefore(tokenData.expiryTime());
    }

    private String hash(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Hashing algorithm not available", e);
        }
    }

    private record TokenData(String tokenValue, LocalDateTime expiryTime, String tokenId) {}
}