// com/camt/dii/secure/audit/AuditLogService.java
package com.camt.dii.secure.audit;

import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;

public interface AuditLogService {
    void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted);
    void logCardModification(Card card, String actionType, String details);
}