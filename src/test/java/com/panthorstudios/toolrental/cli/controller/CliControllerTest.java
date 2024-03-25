package com.panthorstudios.toolrental.cli.controller;
import com.panthorstudios.toolrental.api.service.*;
import com.panthorstudios.toolrental.api.exception.InvalidDiscountPercentException;
import com.panthorstudios.toolrental.api.exception.InvalidRentalDaysException;
import com.panthorstudios.toolrental.api.exception.InvalidToolCodeException;
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
    private InputService inputService;

    @Mock
    private OutputService outputService;

    @InjectMocks
    private CliController cliController;

    /**
     * Test the getValidToolCode method with valid input.
     */
    @Test
    void getValidToolCode_ValidInput() {
        List<String> validToolCodes = List.of("TOOL123");
        when(inputService.readLine()).thenReturn("TOOL123");
        cliController.getValidToolCode(validToolCodes);
        verify(outputService).print("Enter tool code: ");
        verify(checkoutService).validateToolCode("TOOL123");
    }
    /**
     * Test the getValidRentalDays method with valid input.
     */
    void getValidRentalDays_ValidInput() {
        when(inputService.readLine()).thenReturn("5");
        verify(outputService).print("Enter rental days: ");
        verify(checkoutService).validateRentalDays(5);
    }
    /**
     * Test the getValidDiscountPercent method with valid input.
     */
    void getValidDiscountPercent_ValidInput() {
        when(inputService.readLine()).thenReturn("50");
        verify(outputService).print("Enter discount percent: ");
        verify(checkoutService).validateDiscountPercent(50);
    }
    /**
     * Test the getValidCheckoutDate method with valid input.
     */
    void getValidCheckoutDate_ValidInput() {
        when(inputService.readLine()).thenReturn("07/04/21");
        verify(outputService).print("Enter checkout date (mm/dd/yy): ");
        verify(checkoutService).validateCheckoutDate(LocalDate.of(7,4,2021));
    }

    /**
     * Test the getValidToolCode method with invalid input.
     */
    @Test
    void getValidToolCode_InvalidInputThenValid() {
        List<String> validToolCodes = List.of("TOOL123");
        when(inputService.readLine()).thenReturn("INVALID", "TOOL123"); // First invalid, then valid
        doThrow(new InvalidToolCodeException("Invalid code")).when(checkoutService).validateToolCode("INVALID");

        cliController.getValidToolCode(validToolCodes);

        verify(outputService, times(2)).print("Enter tool code: ");
        verify(outputService).printLine("Invalid code");
        verify(checkoutService).validateToolCode("INVALID");
        verify(checkoutService).validateToolCode("TOOL123");
    }


    /**
     * Test the getValidRentalDays method with invalid input.
     */
    @Test
    void getValidRentalDays_InvalidInputThenValid() {
        when(inputService.readLine()).thenReturn("0", "5"); // First invalid, then valid
        doThrow(new InvalidRentalDaysException("Invalid rental days")).when(checkoutService).validateRentalDays(0);

        cliController.getValidRentalDays();

        verify(outputService, times(2)).print("Enter rental days (> 0): ");
        verify(outputService).printLine("Invalid rental days");
        verify(checkoutService).validateRentalDays(0);
        verify(checkoutService).validateRentalDays(5);
    }
    /**
     * Test the getValidDiscountPercent method with invalid input.
     */
    @Test
    void getValidDiscountPercent_InvalidInputThenValid() {
        when(inputService.readLine()).thenReturn("-1", "50"); // First invalid, then valid
        doThrow(new InvalidDiscountPercentException("Invalid discount percent")).when(checkoutService).validateDiscountPercent(-1);

        cliController.getValidDiscountPercent();

        verify(outputService, times(2)).print("Enter discount percent (0-100): ");
        verify(outputService).printLine("Invalid discount percent");
        verify(checkoutService).validateDiscountPercent(-1);
        verify(checkoutService).validateDiscountPercent(50);
    }

    /**
     * Test the getValidCheckoutDate method with invalid input.
     */
    @Test
    void getValidCheckoutDate_InvalidInputThenValid() {
        // Simulate user input: first an invalid date, then a valid date
        when(inputService.readLine()).thenReturn("invalid", "12/31/23");

        LocalDate expectedDate = LocalDate.of(2023, 12, 31);
        LocalDate result = cliController.getValidCheckoutDate();

        assertEquals(expectedDate, result);
        verify(outputService, times(2)).print("Enter checkout date (MM/DD/YY): ");
        verify(outputService).printLine("Invalid checkout date format. Please use MM/DD/YY.");
        verify(outputService, times(2)).print("Enter checkout date (MM/DD/YY): ");
    }

}
