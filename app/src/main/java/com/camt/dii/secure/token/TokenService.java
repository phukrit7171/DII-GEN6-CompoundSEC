package com.camt.dii.secure.token;

public interface TokenService {
    String generateToken(String cardId);
    boolean isValidToken(String cardId, String token);
}