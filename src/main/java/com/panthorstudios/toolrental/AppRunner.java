package com.panthorstudios.toolrental;

import com.panthorstudios.toolrental.cli.controller.CliController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * Application runner
 */
@Component
public class AppRunner implements CommandLineRunner {
    private final CliController cliController;

    @Autowired
    public AppRunner(CliController cliController) {
        this.cliController = cliController;
    }

    @Value("${app.mode}")
    private String mode;

    @Value("${server.port}")
    private int serverPort;

    /**
     * Run the application
     * @param args Command-line arguments
     */
    @Override
    public void run(String... args)  {
        // Check command-line arguments to decide the mode
        if ("web".equals(mode)) {
            System.out.println("Running as a web service on port " + serverPort);
        } else {
            // Run the application in CLI mode
            System.out.println();
            cliController.execute();
        }
    }
}