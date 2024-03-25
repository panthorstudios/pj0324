package com.panthorstudios.toolrental.api.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleInputServiceTest {

    private final InputStream systemIn = System.in;

    /**
     * Redirect stdin from a ByteArrayInputStream
     */
    @BeforeEach
    public void setUpOutput() {
        ByteArrayInputStream testIn = new ByteArrayInputStream("test input".getBytes());
        System.setIn(testIn);
    }

    /**
     * Restore stdin after test(s)
     */
    @AfterEach
    public void restoreSystemInputOutput() {
        System.setIn(systemIn);
    }

    /**
     * Confirm that service gets input correctly
     */
    @Test
    public void readLine_ShouldReturnCorrectInput() {
        // Arrange
        ConsoleInputService inputService = new ConsoleInputService();

        // Act
        String input = inputService.readLine();

        // Assert
        assertEquals("test input", input, "The readLine method should return the correct input.");
    }
}
