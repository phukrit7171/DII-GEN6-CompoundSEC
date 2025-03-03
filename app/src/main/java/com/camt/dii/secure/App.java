package com.camt.dii.secure;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;
import java.util.UUID;

import com.camt.dii.secure.access.Floor;
import com.camt.dii.secure.access.FloorAccessPolicy;
import com.camt.dii.secure.access.FloorAccessService;
import com.camt.dii.secure.access.HighFloorAccess;
import com.camt.dii.secure.access.LowFloorAccess;
import com.camt.dii.secure.access.MediumFloorAccess;
import com.camt.dii.secure.audit.AuditLogger;
import com.camt.dii.secure.audit.AuditLoggerSingleton;
import com.camt.dii.secure.audit.AuditRecord;
import com.camt.dii.secure.audit.decorator.DetailedAuditLogDecorator;
import com.camt.dii.secure.card.AccessCard;
import com.camt.dii.secure.card.CardIdentifier;
import com.camt.dii.secure.card.Permission;
import com.camt.dii.secure.card.SimplePermission;
import com.camt.dii.secure.card.TimeLimitedPermission;
import com.camt.dii.secure.card.factory.CardFactory;
import com.camt.dii.secure.card.factory.StandardCardFactory;

/**
 * Main application that provides an interactive CLI for the access control system.
 */
public class App {
    
    // System components
    private static AuditLogger auditLogger;
    private static AuditLogger detailedLogger;
    private static CardFactory standardFactory;
    private static FloorAccessService floorService;
    
    // Data storage
    private static final Map<String, AccessCard> cardDatabase = new HashMap<>();
    
    // Command scanner
    private static final  Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args) {
        System.out.println("===== Access Control System =====");
        System.out.println("Starting initialization...");
        
        initializeSystem();
        createSampleData();
        
        System.out.println("\nSystem initialized successfully!");
        listCardsForTest();
        runCommandLineInterface();
    }
    
    /**
     * Initialize the core system components.
     */
    private static void initializeSystem() {
        // Initialize audit logging
        auditLogger = AuditLoggerSingleton.getInstance();
        detailedLogger = new DetailedAuditLogDecorator(auditLogger);
        
        // Initialize card factories
        standardFactory = new StandardCardFactory();
        
        // Initialize floor access policies
        FloorAccessPolicy lowPolicy = new LowFloorAccess();
        FloorAccessPolicy mediumPolicy = new MediumFloorAccess(
                LocalTime.of(9, 0),  // 9:00 AM
                LocalTime.of(17, 0)  // 5:00 PM
        );
        FloorAccessPolicy highPolicy = new HighFloorAccess();
        
        // Create floor access service
        floorService = new FloorAccessService(lowPolicy, mediumPolicy, highPolicy);
    }
    
    /**
     * Create sample data for demonstration purposes.
     */
    private static void createSampleData() {
        System.out.println("Creating sample cards...");
        
        // Create permissions
        Set<Floor> allFloors = new HashSet<>(Arrays.asList(Floor.LOW, Floor.MEDIUM, Floor.HIGH));
        Set<Floor> mediumFloors = new HashSet<>(Arrays.asList(Floor.LOW, Floor.MEDIUM));
        Set<Floor> basicFloors = new HashSet<>(Arrays.asList(Floor.LOW));
        Set<String> allRooms = new HashSet<>(Arrays.asList("101", "102", "201", "301"));
        Set<String> basicRooms = new HashSet<>(Arrays.asList("101"));
        
        Permission adminPermission = new SimplePermission(allFloors, allRooms);
        Permission managerPermission = new SimplePermission(mediumFloors, allRooms);
        Permission basicPermission = new SimplePermission(basicFloors, basicRooms);
        Permission visitorPermission = new TimeLimitedPermission(
                basicFloors, 
                basicRooms,
                LocalDateTime.now(),
                LocalDateTime.now().plusDays(1)
        );
        
        // Create cards
        createCard("ADM-001", "ADMIN", adminPermission);
        createCard("MGR-001", "MANAGER", managerPermission);
        createCard("EMP-001", "EMPLOYEE", basicPermission);
        createCard("VIS-001", "VISITOR", visitorPermission);
        
        System.out.println("Sample data created successfully!");
    }
    
    /**
     * Run the interactive command-line interface.
     */
    private static void runCommandLineInterface() {
        boolean running = true;
        
        showHelp();
        
        while (running) {
            System.out.print("\nCommand > ");
            String command;
            if (scanner.hasNextLine()) {
                command = scanner.nextLine().trim();
            } else {
                System.out.println("No more input available. Exiting.");
                break; // Exit the loop if no more input
            }
            
            try {
                if (command.isEmpty()) {
                    continue;
                }
                
                String[] parts = command.split("\\s+", 2);
                String action = parts[0].toLowerCase();
                String params = parts.length > 1 ? parts[1] : "";
                
                switch (action) {
                    case "help" -> showHelp();
                    case "list" -> listCards();
                    case "create" -> handleCreateCard(params);
                    case "info" -> showCardInfo(params);
                    case "access" -> testAccess(params);
                    case "revoke" -> revokeCard(params);
                    case "activate" -> activateCard(params);
                    case "history" -> showAccessHistory(params);
                    case "demo" -> runDemo();
                    case "test-encryption" -> testEncryption(params);
                    case "set-time" -> handleSetTime(params);
                    case "exit", "quit" -> {
                        running = false;
                        System.out.println("Exiting system. Goodbye!");
                    }
                    default -> System.out.println("Unknown command. Type 'help' for available commands.");
                }
            } catch (Exception e) {
                System.out.println("Error executing command: " + e.getMessage());
            }
        }
    }
    
    /**
     * Display help information.
     */
    private static void showHelp() {
        System.out.println("\nAvailable Commands:");
        System.out.println("-------------------");
        System.out.println("help                                - Show this help message");
        System.out.println("list                                - List all cards in the system");
        System.out.println("create <id> <type> <level>          - Create a new card");
        System.out.println("                                      <type>: admin, manager, employee, visitor");
        System.out.println("                                      <level>: high, medium, low");
        System.out.println("info <card-id>                      - Show detailed info for a card");
        System.out.println("access <card-id> <floor>            - Test if a card can access a floor");
        System.out.println("                                      <floor>: high, medium, low");
        System.out.println("revoke <card-id>                    - Revoke a card");
        System.out.println("activate <card-id>                  - Activate a revoked card");
        System.out.println("history <card-id>                   - Show access history for a card");
        System.out.println("demo                                - Run the system demonstration");
        System.out.println("test-encryption <card-id>           - Test the encryption and validation system");
        System.out.println("set-time <floor> <start-time> <end-time> - Set access time for a floor");
        System.out.println("                                      Format: HH:mm (24-hour)");
        System.out.println("exit, quit                          - Exit the system");
    }
    private static void listCardsForTest() {
        for (AccessCard card : cardDatabase.values()) {
            String type = card.getCardId();
            System.out.println(type);
        }
    }
    /**
     * List all cards in the system.
     */
    private static void listCards() {
        if (cardDatabase.isEmpty()) {
            System.out.println("No cards found in the system.");
            return;
        }
        
        System.out.println("\nCards in the system:");
        System.out.println("--------------------");
        System.out.printf("%-15s %-15s %-10s %-20s%n", "CARD ID", "TYPE", "STATUS", "CREATED");
        System.out.println("--------------------------------------------------------------");
        
        for (AccessCard card : cardDatabase.values()) {
            String type = card.getCardId().split("-")[0];
            String status = card.isActive() ? "ACTIVE" : "REVOKED";
            String created = card.getCreationDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            
            System.out.printf("%-15s %-15s %-10s %-20s%n", 
                    card.getFacadeId(), type, status, created);
        }
    }
    
    /**
     * Handle the create card command.
     * 
     * @param params command parameters
     */
    private static void handleCreateCard(String params) {
        String[] parts = params.split("\\s+");
        if (parts.length < 3) {
            System.out.println("Invalid parameters. Usage: create <id> <type> <level>");
            return;
        }
        
        String id = parts[0];
        String type = parts[1].toUpperCase();
        String level = parts[2].toUpperCase();
        
        // Validate type
        if (!Arrays.asList("ADMIN", "MANAGER", "EMPLOYEE", "VISITOR").contains(type)) {
            System.out.println("Invalid card type. Must be one of: admin, manager, employee, visitor");
            return;
        }
        
        // Validate level
        if (!Arrays.asList("HIGH", "MEDIUM", "LOW").contains(level)) {
            System.out.println("Invalid access level. Must be one of: high, medium, low");
            return;
        }
        
        // Create permission based on level
        Permission permission = createPermissionByLevel(parts);
        

        createCard(id, type, permission);
        
        System.out.println("Card created successfully!");
    }
    
    /**
     * Create a permission object based on the specified level.
     * 
     * @param parts the command parts
     * @return the permission object
     */
    private static Permission createPermissionByLevel(String[] parts) {
        Set<Floor> floors = new HashSet<>();
        Set<String> rooms = new HashSet<>();
        
        // Parse floors from command line (3rd argument onwards)
        for (int i = 2; i < parts.length; i++) {
            try {
                floors.add(Floor.valueOf(parts[i].toUpperCase()));
            } catch (IllegalArgumentException e) {
                System.out.println("Warning: Invalid floor ignored: " + parts[i]);
            }
        }
        
        // If no valid floors specified, use default based on type
        if (floors.isEmpty()) {
            switch (parts[1].toUpperCase()) {
                case "ADMIN" -> floors.addAll(Arrays.asList(Floor.values()));
                case "MANAGER" -> {
                    floors.add(Floor.MEDIUM);
                    floors.add(Floor.LOW);
                }
                default -> floors.add(Floor.LOW);
            }
        }
        
        // Add rooms based on granted floors
        if (floors.contains(Floor.HIGH)) {
            rooms.addAll(Arrays.asList("301", "302"));
        }
        if (floors.contains(Floor.MEDIUM)) {
            rooms.addAll(Arrays.asList("201", "202"));
        }
        if (floors.contains(Floor.LOW)) {
            rooms.addAll(Arrays.asList("101", "102"));
        }
        
        return new SimplePermission(floors, rooms);
    }
    
    /**
     * Create a new card and add it to the database.
     * 
     * @param serialNumber the card serial number
     * @param issuer the card issuer
     * @param permission the card permission
     * @param isSecure whether to create a secure card
     * @return the created card
     */
    private static AccessCard createCard(String serialNumber, String issuer, Permission permission) {
        // Create identifier
        CardIdentifier identifier = new CardIdentifier(serialNumber, issuer, LocalDateTime.now());
        
        // Create card using appropriate factory
        AccessCard card = standardFactory.createCard(identifier, permission);
        
        // Log card creation
        detailedLogger.logCardCreation(card.getCardId(), "SYSTEM", LocalDateTime.now());
        
        // Store in database
        cardDatabase.put(card.getFacadeId(), card);
        
        return card;
    }
    
    /**
     * Show detailed information for a card.
     * 
     * @param cardId the card ID
     */
    private static void showCardInfo(String cardId) {
        cardId = hashFacadeId(cardId);
        AccessCard card = findCard(cardId);
        if (card == null) {
            System.out.println("Card not found: " + cardId);
            return;
        }
        
        Permission permission = card.getPermission();
        String cardType = card.getCardId().split("-")[0];
        
        System.out.println("\nCard Information:");
        System.out.println("-----------------");
        System.out.println("Card ID:       " + card.getCardId());
        System.out.println("Type:          " + cardType);
        System.out.println("Status:        " + (card.isActive() ? "ACTIVE" : "REVOKED"));
        System.out.println("Created:       " + card.getCreationDate());
        System.out.println("Last Used:     " + card.getLastUsed());
        System.out.println("Permission:    " + permission.getClass().getSimpleName());
        
        System.out.println("\nAccess Rights:");
        System.out.println("LOW Floor:     " + (card.hasPermission(Floor.LOW) ? "YES" : "NO"));
        System.out.println("MEDIUM Floor:  " + (card.hasPermission(Floor.MEDIUM) ? "YES" : "NO"));
        System.out.println("HIGH Floor:    " + (card.hasPermission(Floor.HIGH) ? "YES" : "NO"));
        
        // Get number of access attempts
        List<AuditRecord> history = auditLogger.getAccessHistory(card.getFacadeId());
        long accessCount = history.stream()
                .filter(record -> "ACCESS_ATTEMPT".equals(record.getEventType().toString()))
                .count();
        
        System.out.println("\nAccess Count:  " + accessCount);
    }
    
    /**
     * Test if a card can access a specific floor.
     * 
     * @param params command parameters
     */
    private static void testAccess(String params) {
        String[] parts = params.split("\\s+");
        if (parts.length < 2) {
            System.out.println("Invalid parameters. Usage: access <card-id> <floor>");
            return;
        }
        
        String cardId = parts[0];
        String floorName = parts[1].toUpperCase();
        
        AccessCard card = findCard(cardId);
        
        if (card == null) {
            System.out.println("Card not found: " + cardId);
            return;
        }
        
        Floor floor;
        try {
            floor = Floor.valueOf(floorName);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid floor. Must be one of: low, medium, high");
            return;
        }
        
        // Test access
        LocalDateTime now = LocalDateTime.now();
        boolean result = floorService.checkAccess(card, floor, now);
        
        // Log the access attempt
        detailedLogger.logAccessAttempt(card.getCardId(), floor.toString(), result, now);
        
        if (result) {
            System.out.println("ACCESS GRANTED: Card " + cardId + " can access " + floorName + " floor at " + now);
        } else {
            System.out.println("ACCESS DENIED: Card " + cardId + " cannot access " + floorName + " floor at " + now);
        }
        
        // Show detailed information about why access was granted or denied
        if (!card.isActive()) {
            System.out.println("Reason: Card is revoked");
        } else if (!card.hasPermission(floor)) {
            System.out.println("Reason: Card does not have permission for this floor");
        } else if (floor == Floor.MEDIUM || floor == Floor.HIGH) {
            if (now.toLocalTime().isBefore(LocalTime.of(9, 0)) ||
                now.toLocalTime().isAfter(LocalTime.of(23, 59))) {
                System.out.println("Reason: Access to this floor is restricted to business hours (9:00 AM - 5:00 PM)");
            }
        }
    }
    
    /**
     * Revoke a card.
     * 
     * @param cardId the card ID
     */
    private static void revokeCard(String cardId) {
        cardId = hashFacadeId(cardId);
        AccessCard card = findCard(cardId);
        if (card == null) {
            System.out.println("Card not found: " + cardId);
            return;
        }
        
        if (!card.isActive()) {
            System.out.println("Card is already revoked: " + cardId);
            return;
        }
        
        card.setActive(false);
        detailedLogger.logCardRevocation(card.getCardId(), "SYSTEM", LocalDateTime.now());
        System.out.println("Card revoked successfully: " + cardId);
    }
    
    /**
     * Activate a card.
     * 
     * @param cardId the card ID
     */
    private static void activateCard(String cardId) {
        cardId = hashFacadeId(cardId);
        AccessCard card = findCard(cardId);
        if (card == null) {
            System.out.println("Card not found: " + cardId);
            return;
        }
        
        if (card.isActive()) {
            System.out.println("Card is already active: " + cardId);
            return;
        }
        
        card.setActive(true);
        detailedLogger.logCardModification(card.getCardId(), "SYSTEM", "Activated card", LocalDateTime.now());
        System.out.println("Card activated successfully: " + cardId);
    }
    
    /**
     * Show access history for a card.
     * 
     * @param cardId the card ID
     */
    private static void showAccessHistory(String cardId) {
        cardId = hashFacadeId(cardId);
        AccessCard card = findCard(cardId);
        if (card == null) {
            System.out.println("Card not found: " + cardId);
            return;
        }
        
        List<AuditRecord> history = auditLogger.getAccessHistory(card.getCardId());
        if (history.isEmpty()) {
            System.out.println("No access history found for card: " + cardId);
            return;
        }
        
        System.out.println("\nAccess History for Card: " + cardId);
        System.out.println("----------------------------");
        System.out.printf("%-25s %-15s %-10s %-15s%n", "TIMESTAMP", "EVENT", "OUTCOME", "LOCATION");
        System.out.println("-----------------------------------------------------------------------");
        
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        
        for (AuditRecord record : history) {
            String timestamp = record.getTimestamp().format(formatter);
            String event = record.getEventType().toString();
            String outcome = record.getOutcome() ? "SUCCESS" : "FAILURE";
            String location = record.getLocation() != null ? record.getLocation() : "-";
            
            System.out.printf("%-25s %-15s %-10s %-15s%n", 
                    timestamp, event, outcome, location);
        }
    }
    
    /**
     * Find a card by ID.
     * 
     * @param cardId the card ID or partial ID
     * @return the card, or null if not found
     */
    private static AccessCard findCard(String cardId) {
        cardId = hashFacadeId(cardId);
        // Direct lookup

        if (cardDatabase.containsKey(cardId)) {
            return cardDatabase.get(cardId);
        }
        
        // Case-insensitive search with partial matching
        for (String key : cardDatabase.keySet()) {
            if (key.toLowerCase().contains(cardId.toLowerCase())) {
                return cardDatabase.get(key);
            }
        }
        
        return null;
    }
    
    /**
     * Tests the encryption and facade ID validation functionality.
     * 
     * @param cardId the ID of the card to test
     */
    private static void testEncryption(String cardId) {
        cardId = hashFacadeId(cardId);
        AccessCard card = findCard(cardId);
        if (card == null) {
            System.out.println("Card not found: " + cardId);
            return;
        }
        
        System.out.println("\n===== Testing Encryption System =====");
        
        // Get current time for testing
        LocalDateTime now = LocalDateTime.now();
        System.out.println("Current time: " + now);
        
        // Test encrypting the card ID
        System.out.println("\n1. Testing ID Encryption:");
        String encryptedId = card.encryptId(card.getCardId(), now);
        System.out.println("Original ID: " + card.getCardId());
        System.out.println("Encrypted ID: " + encryptedId);
        
        // Test facade ID validation
        System.out.println("\n2. Testing Facade ID Validation:");
        // Get first facade ID if we can access it via a method or reflection
        // For demo purposes, we'll create a new facade ID
        String newFacadeId = card.encryptId(card.getCardId() + "-FACADE", now);
        
        System.out.println("Generated Facade ID: " + newFacadeId);
        
        // Test validation with current time
        boolean validNow = card.verifyExternalFacadeId(newFacadeId, now);
        System.out.println("Valid with current time: " + validNow);
        
        // Test validation with future time
        LocalDateTime future = now.plusDays(1);
        boolean validFuture = card.verifyExternalFacadeId(newFacadeId, future);
        System.out.println("Valid with future time (tomorrow): " + validFuture);
        
        // Test with a completely made-up facade ID
        String invalidId = "INVALID-" + UUID.randomUUID().toString();
        boolean validInvalid = card.verifyExternalFacadeId(invalidId, now);
        System.out.println("Valid with invalid ID: " + validInvalid);
        
        System.out.println("\nEncryption testing completed.");
    }
    
    /**
     * Run the system demonstration.
     */
    private static void runDemo() {
        System.out.println("\n===== Running System Demonstration =====");
        
        // Get sample cards
        AccessCard adminCard = findCard("ADM-001");
        AccessCard employeeCard = findCard("EMP-001");
        AccessCard visitorCard = findCard("VIS-001");
        
        if (adminCard == null || employeeCard == null || visitorCard == null) {
            System.out.println("Cannot run demo: Sample cards not found.");
            return;
        }
        
        // Make sure cards are active
        adminCard.setActive(true);
        employeeCard.setActive(true);
        visitorCard.setActive(true);
        
        LocalDateTime now = LocalDateTime.now();
        
        // Demonstrate access attempts
        System.out.println("\nSimulating access attempts:");
        simulateAccess(floorService, adminCard, Floor.LOW, now, "Admin to Low Floor");
        simulateAccess(floorService, adminCard, Floor.MEDIUM, now, "Admin to Medium Floor");
        simulateAccess(floorService, adminCard, Floor.HIGH, now, "Admin to High Floor");
        
        simulateAccess(floorService, employeeCard, Floor.LOW, now, "Employee to Low Floor");
        simulateAccess(floorService, employeeCard, Floor.MEDIUM, now, "Employee to Medium Floor");
        simulateAccess(floorService, employeeCard, Floor.HIGH, now, "Employee to High Floor");
        
        simulateAccess(floorService, visitorCard, Floor.LOW, now, "Visitor to Low Floor");
        simulateAccess(floorService, visitorCard, Floor.MEDIUM, now, "Visitor to Medium Floor");
        simulateAccess(floorService, visitorCard, Floor.HIGH, now, "Visitor to High Floor");
        
        // Demonstrate card revocation
        System.out.println("\nSimulating card revocation:");
        visitorCard.setActive(false);
        detailedLogger.logCardRevocation(visitorCard.getCardId(), "ADMIN", LocalDateTime.now());
        simulateAccess(floorService, visitorCard, Floor.LOW, now, "Revoked Visitor to Low Floor");
        System.out.println(" - Visitor card active status: " + visitorCard.isActive());
        
        System.out.println("\nDemonstration completed successfully!");
    }
    
    /**
     * Simulates an access attempt and prints the result.
     * 
     * @param service the floor access service
     * @param card the access card
     * @param floor the floor being accessed
     * @param time the time of the access attempt
     * @param description description of the access attempt
     */
    private static void simulateAccess(FloorAccessService service, AccessCard card, 
                                     Floor floor, LocalDateTime time, String description) {
        boolean result = service.checkAccess(card, floor, time);
        System.out.println(" - " + description + ": " + (result ? "GRANTED" : "DENIED"));
        
        // Log the access attempt
        detailedLogger.logAccessAttempt(card.getFacadeId(), floor.toString(), result, time);
    }

    private static void handleSetTime(String params) {
        String[] parts = params.split("\\s+");
        if (parts.length < 3) {
            System.out.println("Invalid parameters. Usage: set-time <floor> <start-time> <end-time>");
            return;
        }

        try {
            Floor floor = Floor.valueOf(parts[0].toUpperCase());
            LocalTime startTime = LocalTime.parse(parts[1]);
            LocalTime endTime = LocalTime.parse(parts[2]);

            floorService.setTimeRestriction(floor, startTime, endTime);
            System.out.println("Floor access times updated successfully for " + floor);
            System.out.println("New access hours: " + startTime + " - " + endTime);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid input. Please use format: set-time <floor> HH:mm HH:mm");
        }
    }

    /**
     * Hashes the facade ID using SHA-256.
     * 
     * @param facadeId the facade ID to hash
     * @return the hashed facade ID
     */
    private static String hashFacadeId(String facadeId) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(facadeId.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

}
