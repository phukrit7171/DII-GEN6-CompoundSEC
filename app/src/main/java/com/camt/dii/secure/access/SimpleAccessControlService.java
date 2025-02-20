package com.camt.dii.secure.access;

import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.common.Floor;
import com.camt.dii.secure.management.CardManagementService;
import com.camt.dii.secure.strategy.AccessStrategy;
import com.camt.dii.secure.token.TokenService;

public class SimpleAccessControlService implements AccessControlService {
    private final CardManagementService cardManagementService;
    private final TokenService tokenService;
    private final AccessStrategy accessStrategy;
    private final AuditLogService auditLogService;

    public SimpleAccessControlService(CardManagementService cardManagementService,
                                      TokenService tokenService,
                                      AccessStrategy accessStrategy,
                                      AuditLogService auditLogService) {
        this.cardManagementService = cardManagementService;
        this.tokenService = tokenService;
        this.accessStrategy = accessStrategy;
        this.auditLogService = auditLogService;
    }

    @Override
    public boolean grantAccess(String facadeId, Floor floor, String room, String token) {
        Card card = cardManagementService.findByFacadeId(facadeId);

        if (card == null || !card.isValid()) {
            auditLogService.logAccessAttempt(facadeId, floor, room, false);
            return false;
        }

        boolean tokenValid = tokenService.isValidToken(card.getCardId(), token);
        if (!tokenValid) {
            auditLogService.logAccessAttempt(facadeId, floor, room, false);
            return false;
        }

        boolean accessGranted = accessStrategy.checkAccess(card, floor, room);
        auditLogService.logAccessAttempt(facadeId, floor, room, accessGranted);
        return accessGranted;
    }
}