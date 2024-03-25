package com.panthorstudios.toolrental.cli.controller;
import com.panthorstudios.toolrental.api.service.*;
import com.panthorstudios.toolrental.api.exception.InvalidDiscountPercentException;
import com.panthorstudios.toolrental.api.exception.InvalidRentalDaysException;
import com.panthorstudios.toolrental.api.exception.InvalidToolCodeException;
import com.panthorstudios.toolrental.cli.adapter.InputAdapter;
import com.panthorstudios.toolrental.cli.adapter.OutputAdapter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class CliControllerTest {

    @Mock
    private CheckoutService checkoutService;

    @Mock
    private ToolService toolService;

    @Mock
    private InputAdapter inputAdapter;

    @Mock
    private OutputAdapter outputAdapter;

    @InjectMocks
    private CliController cliController;

    /**
     * Test the getValidToolCode method with valid input.
     */
    @Test
    void getValidToolCode_ValidInput() {
        List<String> validToolCodes = List.of("TOOL123");
        when(inputAdapter.readLine()).thenReturn("TOOL123");
        cliController.getValidToolCode(validToolCodes);
        verify(outputAdapter).print("Enter tool code: ");
        verify(checkoutService).validateToolCode("TOOL123");
    }
    /**
     * Test the getValidRentalDays method with valid input.
     */
    void getValidRentalDays_ValidInput() {
        when(inputAdapter.readLine()).thenReturn("5");
        verify(outputAdapter).print("Enter rental days: ");
        verify(checkoutService).validateRentalDays(5);
    }
    /**
     * Test the getValidDiscountPercent method with valid input.
     */
    void getValidDiscountPercent_ValidInput() {
        when(inputAdapter.readLine()).thenReturn("50");
        verify(outputAdapter).print("Enter discount percent: ");
        verify(checkoutService).validateDiscountPercent(50);
    }
    /**
     * Test the getValidCheckoutDate method with valid input.
     */
    void getValidCheckoutDate_ValidInput() {
        when(inputAdapter.readLine()).thenReturn("07/04/21");
        verify(outputAdapter).print("Enter checkout date (mm/dd/yy): ");
        verify(checkoutService).validateCheckoutDate(LocalDate.of(7,4,2021));
    }

    /**
     * Test the getValidToolCode method with invalid input.
     */
    @Test
    void getValidToolCode_InvalidInputThenValid() {
        List<String> validToolCodes = List.of("TOOL123");
        when(inputAdapter.readLine()).thenReturn("INVALID", "TOOL123"); // First invalid, then valid
        doThrow(new InvalidToolCodeException("Invalid code")).when(checkoutService).validateToolCode("INVALID");

        cliController.getValidToolCode(validToolCodes);

        verify(outputAdapter, times(2)).print("Enter tool code: ");
        verify(outputAdapter).printLine("Invalid code");
        verify(checkoutService).validateToolCode("INVALID");
        verify(checkoutService).validateToolCode("TOOL123");
    }


    /**
     * Test the getValidRentalDays method with invalid input.
     */
    @Test
    void getValidRentalDays_InvalidInputThenValid() {
        when(inputAdapter.readLine()).thenReturn("0", "5"); // First invalid, then valid
        doThrow(new InvalidRentalDaysException("Invalid rental days")).when(checkoutService).validateRentalDays(0);

        cliController.getValidRentalDays();

        verify(outputAdapter, times(2)).print("Enter rental days (> 0): ");
        verify(outputAdapter).printLine("Invalid rental days");
        verify(checkoutService).validateRentalDays(0);
        verify(checkoutService).validateRentalDays(5);
    }
    /**
     * Test the getValidDiscountPercent method with invalid input.
     */
    @Test
    void getValidDiscountPercent_InvalidInputThenValid() {
        when(inputAdapter.readLine()).thenReturn("-1", "50"); // First invalid, then valid
        doThrow(new InvalidDiscountPercentException("Invalid discount percent")).when(checkoutService).validateDiscountPercent(-1);

        cliController.getValidDiscountPercent();

        verify(outputAdapter, times(2)).print("Enter discount percent (0-100): ");
        verify(outputAdapter).printLine("Invalid discount percent");
        verify(checkoutService).validateDiscountPercent(-1);
        verify(checkoutService).validateDiscountPercent(50);
    }

    /**
     * Test the getValidCheckoutDate method with invalid input.
     */
    @Test
    void getValidCheckoutDate_InvalidInputThenValid() {
        // Simulate user input: first an invalid date, then a valid date
        when(inputAdapter.readLine()).thenReturn("invalid", "12/31/23");

        LocalDate expectedDate = LocalDate.of(2023, 12, 31);
        LocalDate result = cliController.getValidCheckoutDate();

        assertEquals(expectedDate, result);
        verify(outputAdapter, times(2)).print("Enter checkout date (MM/DD/YY): ");
        verify(outputAdapter).printLine("Invalid checkout date format. Please use MM/DD/YY.");
        verify(outputAdapter, times(2)).print("Enter checkout date (MM/DD/YY): ");
    }

}
