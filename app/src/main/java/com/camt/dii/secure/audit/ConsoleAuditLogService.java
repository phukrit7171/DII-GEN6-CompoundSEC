package com.camt.dii.secure.audit;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;

public class ConsoleAuditLogService implements AuditLogService {
    private static ConsoleAuditLogService instance = new ConsoleAuditLogService();

    private ConsoleAuditLogService() {}

    public static ConsoleAuditLogService getInstance() {
        return instance;
    }

    @Override
    public void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted) {
        System.out.printf("[LOG] Access Attempt: %s | %s | %s | %s%n", 
            facadeId, floor, room, accessGranted);
    }

    @Override
    public void logCardModification(Card card, String actionType, String details) {
        System.out.printf("[LOG] Card (%s) modified: %s - %s%n", 
            card.getFacadeId(), actionType, details);
    }
}