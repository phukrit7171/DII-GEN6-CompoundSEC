// com/camt/dii/secure/audit/decorator/DetailedAuditLogDecorator.java
package com.camt.dii.secure.audit.decorator;

import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;
import java.time.LocalDateTime;

public class DetailedAuditLogDecorator extends AuditLogServiceDecorator {

    public DetailedAuditLogDecorator(AuditLogService delegate) {
        super(delegate);
    }

    @Override
    public void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted) {
        LocalDateTime timestamp = LocalDateTime.now();
        String status = accessGranted ? "Granted" : "Denied";
        String logMessage = String.format("Detailed Log: [%s] - Access attempt for Facade ID: %s, Floor: %s, Room: %s, Access: %s",
                timestamp, facadeId, floor, room, status);
        System.out.println(logMessage); // Log detailed message to console (or any other enhanced logging)
        super.logAccessAttempt(facadeId, floor, room, accessGranted); // Delegate to the wrapped service for basic logging
    }

    @Override
    public void logCardModification(Card card, String actionType, String details) {
        LocalDateTime timestamp = LocalDateTime.now();
        String logMessage = String.format("Detailed Log: [%s] - Card Modification - Card ID: %s, Action: %s, Details: %s",
                timestamp, card.getCardId(), actionType, details);
        System.out.println(logMessage); // Log detailed card modification info
        super.logCardModification(card, actionType, details); // Delegate to the wrapped service
    }
}