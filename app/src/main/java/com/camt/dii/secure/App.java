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
import java.util.HashMap;
import java.util.Map;

public class App {
    private static final Map<String, String> cardTokens = new HashMap<>();

    public static void main(String[] args) {
        AuditLogService auditLogService = new DetailedAuditLogDecorator(ConsoleAuditLogService.getInstance());
        CardManagementService cardService = new SimpleCardManagementService();
        TokenService tokenService = new SimpleTokenService();
        AccessStrategy accessStrategy = new RuleBasedAccessStrategy();

        AccessControlService accessControlService = new SimpleAccessControlService(
            cardService, tokenService, accessStrategy, auditLogService
        );

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
                        generateToken(scanner, tokenService);
                        break;
                    case 5:
                        requestAccess(scanner, accessControlService);
                        break;
                    case 6:
                        System.out.println("\n[INFO] Audit logs are printed to console in real-time.");
                        break;
                    case 7:
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
        System.out.println("4. Generate Token");
        System.out.println("5. Request Access");
        System.out.println("6. View Audit Logs");
        System.out.println("7. Exit");
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

    private static void generateToken(Scanner scanner, TokenService tokenService) {
        System.out.print("Enter card ID to generate token: ");
        String cardId = scanner.nextLine();
        String token = tokenService.generateToken(cardId);
        cardTokens.put(cardId, token);
        System.out.println("Token generated successfully.");
    }

    private static void requestAccess(Scanner scanner, AccessControlService accessControlService) {
        System.out.print("Enter card facade ID: ");
        String facadeId = scanner.nextLine();
        System.out.print("Floor (LOW, MEDIUM, HIGH): ");
        Floor floor = Floor.valueOf(scanner.nextLine().trim());
        System.out.print("Room: ");
        String room = scanner.nextLine();

        String token = cardTokens.getOrDefault(facadeId, "");
        if (token.isEmpty()) {
            System.out.println("Please generate a token first using option 4");
            return;
        }

        boolean granted = accessControlService.grantAccess(facadeId, floor, room, token);
        System.out.println(granted ? "ACCESS GRANTED" : "ACCESS DENIED");
    }
}