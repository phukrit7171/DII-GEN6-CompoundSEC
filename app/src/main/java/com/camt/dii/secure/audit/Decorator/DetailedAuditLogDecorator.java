package com.camt.dii.secure.audit.Decorator;

import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.common.Floor;

import java.time.LocalDateTime;

public class DetailedAuditLogDecorator extends AuditLogServiceDecorator {
    public DetailedAuditLogDecorator(AuditLogService decorated) {
        super(decorated);
    }

    @Override
    public void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted) {
        System.out.println("\n=== DETAILED AUDIT LOG ===");
        System.out.printf("Timestamp: %s%n", LocalDateTime.now());
        System.out.printf("Card Facade ID: %s%n", facadeId);
        System.out.printf("Location: %s | %s%n", floor, room);
        System.out.printf("Access Decision: %s%n", accessGranted ? "GRANTED" : "DENIED");
        super.logAccessAttempt(facadeId, floor, room, accessGranted);
    }
}