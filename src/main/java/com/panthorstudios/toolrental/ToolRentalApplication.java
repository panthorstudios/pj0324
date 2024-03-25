package com.panthorstudios.toolrental;

import org.apache.commons.cli.*;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ToolRentalApplication {

	/**
	 * Main entry point for the application
	 * @param args Command-line arguments
	 */
	public static void main(String[] args) {
		SpringApplication app = new SpringApplication(ToolRentalApplication.class);
		parseCommandLine(args);

		// Disable web service if web mode is not specified
		if (!"web".equals(System.getProperty("app.mode"))) {
			app.setWebApplicationType(WebApplicationType.NONE);
		}
		app.run(args);
	}

	/**
	 * Parse command-line arguments
	 * @param args Command-line arguments
	 */
	public static void parseCommandLine(String... args) {

		Options options = new Options();

		Option modeOption = new Option("m", "mode", true, "Operating mode (web or cli)");
		modeOption.setRequired(false); // Not required because it has a default value
		options.addOption(modeOption);

		Option storeIdOption = new Option("s", "store", true, "Store ID (integer)");
		storeIdOption.setType(Integer.class);
		storeIdOption.setRequired(false); // Not required because it has a default value
		options.addOption(storeIdOption);

		Option terminalIdOption = new Option("t", "terminal", true, "Terminal ID (integer)");
		terminalIdOption.setType(Integer.class);
		terminalIdOption.setRequired(false); // Not required because it has a default value
		options.addOption(terminalIdOption);

		CommandLineParser parser = new DefaultParser();
		HelpFormatter formatter = new HelpFormatter();

		String mode = null;
		String storeId = null;
		String terminalId = null;

		try {
			CommandLine cmd = parser.parse(options, args);
			mode = cmd.getOptionValue("mode", null);
			storeId = cmd.getOptionValue("store", null);
			terminalId = cmd.getOptionValue("terminal", null);

		} catch (ParseException e) {
			formatter.printHelp("tool-rental", options);
		}

		if (mode!=null) {
			System.setProperty("app.mode", mode);
		}
		if (storeId!=null) {
			System.setProperty("app.store-id", storeId);
		}
		if (terminalId!=null) {
			System.setProperty("app.terminal-id", terminalId);
		}
	}
}
