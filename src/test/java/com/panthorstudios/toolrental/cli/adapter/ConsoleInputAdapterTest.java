package com.panthorstudios.toolrental.cli.adapter;

import com.panthorstudios.toolrental.cli.adapter.ConsoleInputAdapter;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ConsoleInputAdapterTest {

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
        ConsoleInputAdapter inputService = new ConsoleInputAdapter();

        // Act
        String input = inputService.readLine();

        // Assert
        assertEquals("test input", input, "The readLine method should return the correct input.");
    }
}
