// com/camt/dii/secure/access/SimpleAccessControlService.java
package com.camt.dii.secure.access;

import com.camt.dii.secure.access.strategy.AccessStrategy;
import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.card.Card;
import com.camt.dii.secure.card.Permission;
import com.camt.dii.secure.card.SimplePermission;
import com.camt.dii.secure.card.SimpleCard;
import com.camt.dii.secure.common.Floor;
import com.camt.dii.secure.management.CardManagementService;
import com.camt.dii.secure.token.TokenService;

public class SimpleAccessControlService implements AccessControlService {

    private final AuditLogService auditLogService;
    private final CardManagementService cardManagementService;
    private final TokenService tokenService;
    private final AccessStrategy accessStrategy; // Strategy pattern

    public SimpleAccessControlService(AuditLogService auditLogService, CardManagementService cardManagementService, TokenService tokenService, AccessStrategy accessStrategy) {
        this.auditLogService = auditLogService;
        this.cardManagementService = cardManagementService;
        this.tokenService = tokenService;
        this.accessStrategy = accessStrategy;
    }

    @Override
    public boolean grantAccess(String facadeId, Floor floor, String room, String token) {
        boolean accessGranted = false;
        Card card = findCardByFacadeId(facadeId);

        if (card.isRevoked()) {
            auditLogService.logAccessAttempt(facadeId, floor, room, false);
            return false;
        }

        if (tokenService.isValidToken(card.getCardId(), token)) { // Validate token first
            accessGranted = accessStrategy.checkAccess(card, floor, room); // Delegate to Strategy
        }

        auditLogService.logAccessAttempt(facadeId, floor, room, accessGranted);
        return accessGranted;
    }

    private Card findCardByFacadeId(String facadeId) {
        // Use CardManagementService to find the card
        Card card = cardManagementService.findCardByFacadeId(facadeId);
        
        // If no card found, create a fallback admin card
        if (card == null) {
            Permission adminPermission = new SimplePermission(true, true, true, "AllRooms");
            card = new SimpleCard("admin123", adminPermission);
        }
        
        return card;
    }
}