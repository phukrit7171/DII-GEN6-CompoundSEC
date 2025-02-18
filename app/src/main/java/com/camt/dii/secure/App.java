package com.camt.dii.secure;

import com.camt.dii.secure.access.AccessControlService;
import com.camt.dii.secure.access.SimpleAccessControlService;
import com.camt.dii.secure.access.strategy.TimeBasedAccessStrategy;
import com.camt.dii.secure.token.SimpleTokenService;
import com.camt.dii.secure.ui.cli.Main;

public class App {
    public static void main(String[] args) {
        // Initialize services
        TimeBasedAccessStrategy accessStrategy = new TimeBasedAccessStrategy();
        SimpleTokenService tokenService = new SimpleTokenService("your-secure-secret-key-here");

        AccessControlService accessControlService = new SimpleAccessControlService(
                null, // AuditLogService
                null, // CardManagementService
                tokenService,
                accessStrategy);

        // Start CLI
        try (Main cli = new Main(accessControlService)) {
            cli.start();
        }
    }
}