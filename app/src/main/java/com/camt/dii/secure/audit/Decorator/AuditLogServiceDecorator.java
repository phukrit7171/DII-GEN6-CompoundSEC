package com.camt.dii.secure.audit.Decorator;

import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;

public abstract class AuditLogServiceDecorator implements AuditLogService {
    protected final AuditLogService decorated;

    public AuditLogServiceDecorator(AuditLogService decorated) {
        this.decorated = decorated;
    }

    @Override
    public void logAccessAttempt(String facadeId, Floor floor, String room, boolean accessGranted) {
        decorated.logAccessAttempt(facadeId, floor, room, accessGranted);
    }

    @Override
    public void logCardModification(Card card, String actionType, String details) {
        decorated.logCardModification(card, actionType, details);
    }
}
