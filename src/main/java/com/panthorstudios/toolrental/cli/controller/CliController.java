package com.panthorstudios.toolrental.cli.controller;

import com.panthorstudios.toolrental.api.domain.RentalAgreement;
import com.panthorstudios.toolrental.api.service.*;
import com.panthorstudios.toolrental.api.exception.InvalidCheckoutDateException;
import com.panthorstudios.toolrental.api.exception.InvalidDiscountPercentException;
import com.panthorstudios.toolrental.api.exception.InvalidRentalDaysException;
import com.panthorstudios.toolrental.api.exception.InvalidToolCodeException;
import com.panthorstudios.toolrental.cli.adapter.InputAdapter;
import com.panthorstudios.toolrental.cli.adapter.OutputAdapter;
import com.panthorstudios.toolrental.util.FormattingTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

@Component
public class CliController {

    private final CheckoutService checkoutService;
    private final ToolService toolService;
    private final InputAdapter inputAdapter;
    private final OutputAdapter outputAdapter;

    @Autowired
    public CliController(CheckoutService checkoutService, ToolService toolService, InputAdapter inputAdapter, OutputAdapter outputAdapter) {
        this.checkoutService = checkoutService;
        this.toolService = toolService;
        this.inputAdapter = inputAdapter;
        this.outputAdapter = outputAdapter;
    }

    /**
     * Executes the CLI controller
     */
    public void execute() {
        outputAdapter.printLine("Rental Agreement Generator");
        outputAdapter.printLine("--------------------------");
        outputAdapter.printLine();
        List<String> validToolCodes = toolService.getAllToolCodes();
        // Prompt the user for input
        do {
            try {
                String toolCode = getValidToolCode(validToolCodes);
                int rentalDays = getValidRentalDays();
                int discountPercent = getValidDiscountPercent();
                LocalDate checkoutDate = getValidCheckoutDate();

                // Create a rental agreement with the provided input
                RentalAgreement rentalAgreement = checkoutService.toolRentalCheckout(
                        toolCode, checkoutDate, rentalDays, discountPercent);

                outputAdapter.printLine();
                outputAdapter.printLine("Rental Agreement");
                outputAdapter.printLine("----------------");

                // use method in RentalAgreement to print to console as per requirement
                rentalAgreement.print(outputAdapter);

            } catch (Exception e) {
                outputAdapter.printLine("An error occurred: " + e.getMessage());
            }
        } while (promptForAnother());
        System.exit(0);

    }

    /**
     * Prompts the user for a valid tool code
     *
     * @param validToolCodes the valid tool codes
     * @return a valid tool code
     */
    public String getValidToolCode(List<String> validToolCodes) {
        while (true) {
            Set<String> toolCodesSet = new LinkedHashSet<>(validToolCodes);
            String toolCode = promptForToolCode();
            if (validateToolCode(toolCode, toolCodesSet)) {
                return toolCode;
            }
        }
    }

    /**
     * Prompts the user for a valid tool code
     *
     * @return a valid tool code
     */
    private String promptForToolCode() {
        outputAdapter.print("Enter tool code: ");
        return inputAdapter.readLine();
    }

    /** Validates the tool code.
     *
     * @param toolCode the tool code
     * @param validToolCodes the valid tool codes
     * @return true if the tool code is valid, false otherwise
     */
    private boolean validateToolCode(String toolCode, Set<String> validToolCodes) {
        try {
            checkoutService.validateToolCode(toolCode);
            return true;
        } catch (InvalidToolCodeException e) {
            outputAdapter.printLine(e.getMessage());
            displayValidToolCodes(validToolCodes);
            return false;
        }
    }

    /**
     * Displays the valid tool codes to the user
     *
     * @param validToolCodes the valid tool codes
     */
    private void displayValidToolCodes(Set<String> validToolCodes) {
        outputAdapter.printLine("Valid codes:");
        validToolCodes.forEach(outputAdapter::printLine);
        outputAdapter.printLine();
    }

    /**
     * Prompts the user for a valid checkout date
     *
     * @return a valid checkout date
     */
    public LocalDate getValidCheckoutDate() {
        while (true) {
            String dateString = promptForCheckoutDate();
            LocalDate checkoutDate = parseDate(dateString);
            if (checkoutDate != null && validateCheckoutDate(checkoutDate)) {
                return checkoutDate;
            }
        }
    }

    /**
     * Parses a date string
     *
     * @param dateString the date string
     * @return the parsed date
     */
    private LocalDate parseDate(String dateString) {
        try {
            return LocalDate.parse(dateString, FormattingTools.SHORT_DATE_FORMATTER);
        } catch (DateTimeParseException e) {
            outputAdapter.printLine("Invalid checkout date format. Please use MM/DD/YY.");
            return null;
        }
    }

    /**
     * Validates the checkout date
     *
     * @param date the checkout date
     * @return true if the checkout date is valid, false otherwise
     */
    private boolean validateCheckoutDate(LocalDate date) {
        try {
            checkoutService.validateCheckoutDate(date);
            return true;
        } catch (InvalidCheckoutDateException e) { // Assuming this is a custom exception
            outputAdapter.printLine(e.getMessage());
            return false;
        }
    }

    /**
     * Prompts the user for a valid checkout date
     *
     * @return a valid checkout date
     */
    private String promptForCheckoutDate() {
        outputAdapter.print("Enter checkout date (MM/DD/YY): ");
        return inputAdapter.readLine();
    }

    /**
     * Prompts the user for a valid number of rental days
     *
     * @return a valid number of rental days
     */
    private String promptForRentalDays() {
        outputAdapter.print("Enter rental days (> 0): ");
        return inputAdapter.readLine();
    }
    /**
     * Parses a rental days string
     *
     * @param input the rental days string
     * @return the parsed rental days
     */
    private Integer parseRentalDays(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            outputAdapter.printLine("Invalid rental days. Please enter a positive integer.");
            return null;
        }
    }

    /**
     * Validates the rental days
     *
     * @param rentalDays the rental days
     * @return true if the rental days are valid, false otherwise
     */
    private boolean validateRentalDays(int rentalDays) {
        try {
            checkoutService.validateRentalDays(rentalDays);
            return true;
        } catch (InvalidRentalDaysException e) {
            outputAdapter.printLine(e.getMessage());
            return false;
        }
    }

    /**
     * Prompts the user for a valid rental days
     *
     * @return a valid rental days
     */
    public int getValidRentalDays() {
        while (true) {
            String inputString = promptForRentalDays();
            Integer parsedDays = parseRentalDays(inputString);
            if (parsedDays != null && validateRentalDays(parsedDays)) {
                return parsedDays;
            }
        }
    }

    /**
     * Prompts the user for a valid discount percent
     *
     * @return a valid discount percent
     */
    public int getValidDiscountPercent() {
        while (true) {
            String inputString = promptForDiscountPercent();
            Integer parsedPercent = parseDiscountPercent(inputString);
            if (parsedPercent != null && validateDiscountPercent(parsedPercent)) {
                return parsedPercent;
            }
        }
    }

    /**
     * Prompts the user for a valid discount percent
     *
     * @return a valid discount percent
     */
    private String promptForDiscountPercent() {
        outputAdapter.print("Enter discount percent (0-100): ");
        return inputAdapter.readLine();
    }

    /**
     * Parses a discount percent string
     *
     * @param input the discount percent string
     * @return the parsed discount percent
     */
    private Integer parseDiscountPercent(String input) {
        try {
            return Integer.parseInt(input);
        } catch (NumberFormatException e) {
            outputAdapter.printLine("Invalid discount percent. Please enter a number between 0 and 100.");
            return null;
        }
    }

    /**
     * Validates the discount percent
     *
     * @param percent the discount percent
     * @return true if the discount percent is valid, false otherwise
     */
    private boolean validateDiscountPercent(int percent) {
        try {
            checkoutService.validateDiscountPercent(percent);
            return true;
        } catch (InvalidDiscountPercentException e) { // Assume this is a specific checked exception
            outputAdapter.printLine(e.getMessage());
            return false;
        }
    }
    /**
     * Prompts the user for another rental agreement
     *
     * @return true if the user wants to enter another rental agreement, false otherwise
     */
    private boolean promptForAnother() {
        outputAdapter.print("Hit Return for another Rental Agreement or 'q' to quit: ");
        String cmdString = inputAdapter.readLine();
        outputAdapter.printLine();
        return !cmdString.equals("q");
    }
}
