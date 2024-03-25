package com.panthorstudios.toolrental.api.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleOutputServiceTest {

    private final PrintStream originalOut = System.out;
    private final PrintStream originalErr = System.err;
    private ByteArrayOutputStream outContent;
    private ByteArrayOutputStream errContent;

    /**
     * Redirect stdin/stdout to ByteArrayOutputStream
     */
    @BeforeEach
    public void setUpStreams() {
        outContent = new ByteArrayOutputStream();
        errContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    /**
     * Restore stdin/stdout
     */
    @AfterEach
    public void restoreStreams() {
        System.setOut(originalOut);
        System.setErr(originalErr);
    }

    /**
     * Test that messages to stdout are done properly without newline
     */
    @Test
    public void print_ShouldPrintMessage() {
        // Arrange
        ConsoleOutputService outputService = new ConsoleOutputService();
        String message = "Hello, World!";

        // Act
        outputService.print(message);

        // Assert
        assertEquals(message, outContent.toString(), "The print method should output the correct message.");
    }
    /**
     * Test that messages to stdout are done properly for newline only
     */
    @Test
    public void printLine_ShouldPrintNewLine() {
        // Arrange
        ConsoleOutputService outputService = new ConsoleOutputService();

        // Act
        outputService.printLine();

        // Assert
        assertEquals(System.lineSeparator(), outContent.toString(), "The printLine method should output a new line.");
    }
    /**
     * Test that messages to stdout are done properly with newline
     */
    @Test
    public void printLineWithMessage_ShouldPrintMessageWithNewLine() {
        // Arrange
        ConsoleOutputService outputService = new ConsoleOutputService();
        String message = "Test message";

        // Act
        outputService.printLine(message);

        // Assert
        assertEquals(message + System.lineSeparator(), outContent.toString(), "The printLine method should output the message followed by a new line.");
    }
    /**
     * Test that messages to stderr are done properly with newline
     */
    @Test
    public void printError_ShouldPrintErrorMessage() {
        // Arrange
        ConsoleOutputService outputService = new ConsoleOutputService();
        String errorMessage = "Error message";

        // Act
        outputService.printError(errorMessage);

        // Assert
        assertEquals(errorMessage + System.lineSeparator(), errContent.toString(), "The printError method should output the error message to System.err.");
    }
}
