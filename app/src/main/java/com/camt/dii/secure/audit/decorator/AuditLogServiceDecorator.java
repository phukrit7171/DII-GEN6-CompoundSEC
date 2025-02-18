// com/camt/dii/secure/audit/decorator/AuditLogServiceDecorator.java
package com.camt.dii.secure.audit.decorator;

import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;

public abstract class AuditLogServiceDecorator implements AuditLogService {
    protected final AuditLogService delegate;

    public AuditLogServiceDecorator(AuditLogService delegate) {
        this.delegate = delegate;
    }

    @Override
    public void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted) {
        delegate.logAccessAttempt(facadeId, floor, room, accessGranted);
    }

    @Override
    public void logCardModification(Card card, String actionType, String details) {
        delegate.logCardModification(card, actionType, details);
    }
}