package com.camt.dii.secure;

import com.camt.dii.secure.access.AccessControlService;
import com.camt.dii.secure.access.SimpleAccessControlService;
import com.camt.dii.secure.audit.AuditLogService;
import com.camt.dii.secure.audit.ConsoleAuditLogService;
import com.camt.dii.secure.audit.Decorator.DetailedAuditLogDecorator;
import com.camt.dii.secure.card.*;
import com.camt.dii.secure.card.Permission.Permission;
import com.camt.dii.secure.card.Permission.SimplePermission;
import com.camt.dii.secure.common.Floor;
import com.camt.dii.secure.management.CardManagementService;
import com.camt.dii.secure.management.SimpleCardManagementService;
import com.camt.dii.secure.strategy.AccessStrategy;
import com.camt.dii.secure.strategy.RuleBasedAccessStrategy;
import com.camt.dii.secure.token.SimpleTokenService;
import com.camt.dii.secure.token.TokenService;

import java.util.*;
import java.util.stream.Collectors;

public class App {
    // Static service instances to maintain state across runs
    private static final CardManagementService cardService = new SimpleCardManagementService();
    private static final TokenService tokenService = new SimpleTokenService();
    private static final AccessStrategy accessStrategy = new RuleBasedAccessStrategy();
    private static final AuditLogService auditLogService = 
        new DetailedAuditLogDecorator(ConsoleAuditLogService.getInstance());
    private static final AccessControlService accessControlService = 
        new SimpleAccessControlService(cardService, tokenService, accessStrategy, auditLogService);

    public static void main(String[] args) {
        // Remove service instantiation since we're using static instances
        Scanner scanner = new Scanner(System.in);
        try {
            while (true) {
                displayMenu();
                if (!scanner.hasNextInt()) {
                    break;
                }
                int choice = scanner.nextInt();
                scanner.nextLine();  // Consume newline

                switch (choice) {
                    case 1:
                        addCard(scanner, cardService);
                        break;
                    case 2:
                        revokeCard(scanner, cardService);
                        break;
                    case 3:
                        modifyPermissions(scanner, cardService);
                        break;
                    case 4:
                        requestAccess(scanner, accessControlService, tokenService, cardService);
                        break;
                    case 5:
                        System.out.println("\n[INFO] Audit logs are printed to console in real-time.");
                        break;
                    case 6:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } finally {
            scanner.close();
        }
    }

    private static void displayMenu() {
        System.out.println("\n=== SECURE FACILITY CLI ===");
        System.out.println("1. Add Card");
        System.out.println("2. Revoke Card");
        System.out.println("3. Modify Card Permissions");
        System.out.println("4. Request Access");
        System.out.println("5. View Audit Logs");
        System.out.println("6. Exit");
        System.out.print("Choose option: ");
    }

    private static void addCard(Scanner scanner, CardManagementService cardService) {
        System.out.print("Enter card ID: ");
        String cardId = scanner.nextLine();

        System.out.print("Allowed floors (LOW, MEDIUM, HIGH; comma-separated): ");
        String floorStr = scanner.nextLine();
        Set<Floor> floors = Arrays.stream(floorStr.split(","))
                                  .map(String::trim)
                                  .map(Floor::valueOf)
                                  .collect(Collectors.toSet());

        System.out.print("Allowed rooms (comma-separated): ");
        String roomStr = scanner.nextLine();
        Set<String> rooms = new HashSet<>(Arrays.asList(roomStr.split(",")));

        Permission permissions = new SimplePermission(floors, rooms);
        Card card = new SimpleCard(cardId, permissions);
        cardService.addCard(card);

        System.out.println("Card added successfully.");
        System.out.println("Facade ID for this card is: " + card.getFacadeId());
    }

    private static void revokeCard(Scanner scanner, CardManagementService cardService) {
        System.out.print("Enter card facade ID to revoke: ");
        String facadeId = scanner.nextLine();
        cardService.revokeCard(facadeId);
        System.out.println("Card revoked.");
    }

    private static void modifyPermissions(Scanner scanner, CardManagementService cardService) {
        System.out.println("* Feature not implemented *");
    }

    private static void requestAccess(Scanner scanner, AccessControlService accessControlService, 
                                    TokenService tokenService, CardManagementService cardService) {
        try {
            System.out.print("Enter card facade ID: ");
            String facadeId = scanner.nextLine().trim();
            if (facadeId.isEmpty()) {
                System.out.println("Facade ID cannot be empty");
                return;
            }

            System.out.print("Floor (LOW, MEDIUM, HIGH): ");
            String floorInput = scanner.nextLine().trim().toUpperCase();
            Floor floor;
            try {
                floor = Floor.valueOf(floorInput);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid floor level. Please use LOW, MEDIUM, or HIGH");
                return;
            }

            System.out.print("Room: ");
            String room = scanner.nextLine().trim();
            if (room.isEmpty()) {
                System.out.println("Room cannot be empty");
                return;
            }

            // Get card and generate token automatically
            Card card = cardService.findByFacadeId(facadeId);
            if (card == null) {
                System.out.println("Card not found. Please check the facade ID");
                return;
            }

            // Generate token automatically
            String token = tokenService.generateToken(card.getCardId());
            
            boolean granted = accessControlService.grantAccess(facadeId, floor, room, token);
            System.out.println(granted ? "ACCESS GRANTED" : "ACCESS DENIED");
        } catch (Exception e) {
            System.out.println("An error occurred: " + e.getMessage());
        }
    }
}