// com/camt/dii/secure/audit/ConsoleAuditLogService.java
package com.camt.dii.secure.audit;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;
import java.time.LocalDateTime;

public class ConsoleAuditLogService implements AuditLogService {

    private static volatile ConsoleAuditLogService instance; // Singleton instance

    private ConsoleAuditLogService() {} // Private constructor for Singleton

    public static ConsoleAuditLogService getInstance() { // Singleton access method
        if (instance == null) {
            synchronized (ConsoleAuditLogService.class) {
                if (instance == null) {
                    instance = new ConsoleAuditLogService();
                }
            }
        }
        return instance;
    }


    @Override
    public void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted) {
        LocalDateTime timestamp = LocalDateTime.now();
        String status = accessGranted ? "Granted" : "Denied";
        System.out.println(timestamp + " - Access attempt for Facade ID: " + facadeId +
                           ", Floor: " + floor + ", Room: " + room + ", Access: " + status);
    }

    @Override
    public void logCardModification(Card card, String actionType, String details) {
        LocalDateTime timestamp = LocalDateTime.now();
        System.out.println(timestamp + " - Card Modification - Card ID: " + card.getCardId() +
                           ", Action: " + actionType + ", Details: " + details);
    }
}