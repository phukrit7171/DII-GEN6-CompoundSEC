package com.camt.dii.secure.ui.cli;

import com.camt.dii.secure.access.AccessControlService;
import com.camt.dii.secure.common.Floor;
import java.util.Scanner;

public class Main implements AutoCloseable {
    private final AccessControlService accessControlService;
    private final Scanner scanner;

    public Main(AccessControlService accessControlService) {
        this.accessControlService = accessControlService;
        this.scanner = new Scanner(System.in);
    }

    @Override
    public void close() {
        scanner.close();
    }

    public void start() {
        try {
            while (true) {
                printMenu();
                if (!scanner.hasNextLine()) {
                    break;
                }
                String choice = scanner.nextLine().trim();

                switch (choice) {
                    case "1":
                        handleAccessRequest();
                        break;
                    case "2":
                        displayHelp();
                        break;
                    case "3":
                        System.out.println("Exiting system. Goodbye!");
                        return;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            }
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

    private void printMenu() {
        System.out.println("\n=== Secure Access Control System ===");
        System.out.println("1. Request Access");
        System.out.println("2. Help");
        System.out.println("3. Exit");
        System.out.print("Enter your choice (1-3): ");
    }

    private void handleAccessRequest() {
        System.out.print("Enter Facade ID: ");
        String facadeId = scanner.nextLine().trim();

        System.out.print("Enter Floor (GROUND, FIRST, SECOND, THIRD): ");
        String floorInput = scanner.nextLine().trim().toUpperCase();
        Floor floor;
        try {
            floor = Floor.valueOf(floorInput);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid floor. Please use GROUND, FIRST, SECOND, or THIRD.");
            return;
        }

        System.out.print("Enter Room Number: ");
        String room = scanner.nextLine().trim();

        System.out.print("Enter Access Token: ");
        String token = scanner.nextLine().trim();

        boolean accessGranted = accessControlService.grantAccess(facadeId, floor, room, token);

        if (accessGranted) {
            System.out.println("Access granted to room " + room + " on floor " + floor);
        } else {
            System.out.println("Access denied. Please check your credentials and try again.");
        }
    }

    private void displayHelp() {
        System.out.println("\n=== Help Information ===");
        System.out.println("This system controls access to various rooms in the building.");
        System.out.println("To request access, you need:");
        System.out.println("1. Your Facade ID (unique identifier for your card)");
        System.out.println("2. The floor you want to access (GROUND, FIRST, SECOND, THIRD)");
        System.out.println("3. The room number you want to access");
        System.out.println("4. A valid access token");
        System.out.println("\nIf you have any issues, please contact system administrator.");
    }
}
