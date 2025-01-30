package com.securitysystem.main;

package com.securitysystem.main;

import com.securitysystem.admin.CommandLineUI;

public class Main {

    public static void main(String[] args) {
        System.out.println("Starting Compound Security System...");

        // Initialize and start the system components here
        CommandLineUI ui = new CommandLineUI(); // Example: Starting command-line UI
        ui.start();

        System.out.println("Compound Security System Initialized.");
    }
}
