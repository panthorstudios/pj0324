package com.panthorstudios.toolrental.api.service;

import com.panthorstudios.toolrental.api.domain.RentalAgreement;
import com.panthorstudios.toolrental.api.domain.Tool;
import com.panthorstudios.toolrental.api.domain.ToolType;
import com.panthorstudios.toolrental.api.exception.InvalidCheckoutDateException;
import com.panthorstudios.toolrental.api.exception.InvalidDiscountPercentException;
import com.panthorstudios.toolrental.api.exception.InvalidRentalDaysException;
import com.panthorstudios.toolrental.api.exception.InvalidToolCodeException;
import com.panthorstudios.toolrental.properties.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    @Mock
    private ToolService mockToolService;

    @Mock
    private HolidayService mockHolidayService;

    @Mock
    private AppProperties mockAppProperties;

    private CheckoutService checkoutService;

    @BeforeEach
    void setUp() {
        checkoutService = new CheckoutService(mockToolService, mockHolidayService, mockAppProperties);
    }

    @Test
    void testValidCheckout() {
        // Setup
        String toolCode = "CHNS";
        String toolTypeCode = "CHAINSAW";
        String toolTypeLabel = "Chainsaw";
        String toolBrand = "Stihl";
        BigDecimal dailyCharge =  new BigDecimal("1.49");
        LocalDate checkoutDate = LocalDate.of(2023, 4, 1);
        int rentalDays = 5;
        int discountPercent = 10;

        when(mockToolService.getToolByCode(toolCode)).thenReturn(Optional.of(new Tool(toolCode, toolTypeCode, toolBrand)));
        when(mockToolService.getToolTypeByTool(any(Tool.class))).thenReturn(Optional.of(new ToolType(toolTypeCode, toolTypeLabel, dailyCharge, true, false, true)));
        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        when(mockHolidayService.getHolidays(anyInt(), anyInt())).thenReturn(Set.of(LocalDate.of(2023, 4, 4)));
        when(mockAppProperties.getStoreId()).thenReturn(1);
        when(mockAppProperties.getTerminalId()).thenReturn(1);

        // Execute
        RentalAgreement rentalAgreement = checkoutService.toolRentalCheckout(toolCode, checkoutDate, rentalDays, discountPercent);

        // Verify
        assertNotNull(rentalAgreement);
        assertEquals(toolCode, rentalAgreement.toolCode());
        assertEquals(toolTypeLabel, rentalAgreement.toolType());
        assertEquals(toolBrand, rentalAgreement.toolBrand());
        assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.dueDate());
    }

    @Test
    void toolRentalCheckout_WithValidParameters_ReturnsRentalAgreement() {
        String toolCode = "LADW";
        String toolTypeCode = "LADDER";
        String toolBrand = "Werner";
        String toolTypeLabel = "Ladder";
        BigDecimal dailyCharge = BigDecimal.valueOf(1.99);
        LocalDate checkoutDate = LocalDate.of(2023, 7, 2);
        int rentalDays = 5;
        int discountPercent = 10;

        Tool tool = new Tool(toolCode, toolTypeCode, toolBrand);
        ToolType toolType = new ToolType(toolTypeCode, toolTypeLabel, dailyCharge, true, true, false);

        when(mockToolService.getToolByCode(toolCode)).thenReturn(Optional.of(tool));
        when(mockToolService.getToolTypeByTool(tool)).thenReturn(Optional.of(toolType));
        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        when(mockHolidayService.getHolidays(anyInt(), anyInt())).thenReturn(new HashSet<>());

        RentalAgreement rentalAgreement = checkoutService.toolRentalCheckout(toolCode, checkoutDate, rentalDays, discountPercent);

        assertNotNull(rentalAgreement);
        assertEquals(toolCode, rentalAgreement.toolCode());
        assertEquals(toolTypeLabel, rentalAgreement.toolType());
        assertEquals(toolBrand, rentalAgreement.toolBrand());
        assertEquals(checkoutDate.plusDays(rentalDays), rentalAgreement.dueDate());
    }

    @Test
    void toolRentalCheckoutWithNonexistentToolCode() {

        String invalidToolCode = "INVALID";
        Exception exception = assertThrows(InvalidToolCodeException.class, () ->
                checkoutService.toolRentalCheckout(invalidToolCode, LocalDate.now(), 4, 10));

        assertTrue(exception.getMessage().contains("Tool code is not valid"));
    }

    @Test
    void testInvalidCheckoutDate() {
        String toolCode = "CHNS";
        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        assertThrows(InvalidCheckoutDateException.class, () ->
                checkoutService.toolRentalCheckout(toolCode, null, 5, 10));
    }

    @Test
    void testInvalidRentalDays() {
        String toolCode = "CHNS";
        when(mockToolService.toolExists(toolCode)).thenReturn(true);

        assertThrows(InvalidRentalDaysException.class, () ->
                checkoutService.toolRentalCheckout(toolCode, LocalDate.now(), 0, 10));
    }
    @Test
    void testInvalidDiscountPercent() {
        String toolCode = "CHNS";
        int rentalDays = 5;

        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        assertThrows(InvalidDiscountPercentException.class, () ->
                checkoutService.toolRentalCheckout(toolCode, LocalDate.now(), rentalDays, -1));
        assertThrows(InvalidDiscountPercentException.class, () ->
                checkoutService.toolRentalCheckout(toolCode, LocalDate.now(), rentalDays, 101));
    }
    @Test
    void testChargeCalculation() {
        String toolCode = "CHNS";
        String typeCode = "CHAINSAW"; // charges don't apply on holidays
        BigDecimal dailyCharge = new BigDecimal("1.49");

        LocalDate checkoutDate = LocalDate.of(2024, 3, 16); // Saturday
        int rentalDays = 5;
        int discountPercent = 10;

        // Assume these are valid inputs and setup necessary mock responses
        // Mock toolService, holidayService, and appProperties as necessary
        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        when(mockToolService.getToolByCode(toolCode)).thenReturn(Optional.of(new Tool(toolCode, typeCode, "Stihl")));
        when(mockToolService.getToolTypeByTool(any(Tool.class))).thenReturn(Optional.of(new ToolType(typeCode, "Chainsaw", dailyCharge, true, false, true)));
        when(mockAppProperties.getStoreId()).thenReturn(1);
        when(mockAppProperties.getTerminalId()).thenReturn(1);

        when(mockHolidayService.getHolidays(anyInt(), anyInt())).thenReturn(Set.of(LocalDate.of(2023, 7, 4))); // Example holiday

        // Execute
        RentalAgreement agreement = checkoutService.toolRentalCheckout(toolCode, checkoutDate, rentalDays, discountPercent);

        // Verify
        assertNotNull(agreement);
        assertEquals(agreement.dailyRentalCharge(), dailyCharge);
        assertEquals(agreement.chargeDays(), 4); // Sunday doesn't count
        assertEquals(agreement.preDiscountCharge(), new BigDecimal("5.96")); // 1.49*4
        assertEquals(agreement.discountAmount(), new BigDecimal("0.60"));
        assertEquals(agreement.finalCharge(), new BigDecimal("5.36"));

        toolCode = "LADW";
        typeCode = "LADDER"; // charges don't apply on weekends
        checkoutDate = LocalDate.of(2024, 6, 30); // Sunday, test holiday
        dailyCharge = new BigDecimal("1.99");
        rentalDays = 7;

        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        when(mockToolService.getToolByCode(toolCode)).thenReturn(Optional.of(new Tool(toolCode, typeCode, "Werner")));
        when(mockToolService.getToolTypeByTool(any(Tool.class))).thenReturn(Optional.of(new ToolType(typeCode, "Ladder", dailyCharge, true, false, true)));

        // Execute
        agreement = checkoutService.toolRentalCheckout(toolCode, checkoutDate, rentalDays, discountPercent);

        // Verify
        assertNotNull(agreement);
        assertEquals(agreement.dailyRentalCharge(), dailyCharge);
        assertEquals(agreement.chargeDays(), 5); // Sunday doesn't count
        assertEquals(agreement.preDiscountCharge(), new BigDecimal("9.95"));
        assertEquals(agreement.discountAmount(), new BigDecimal("1.00"));
        assertEquals(agreement.finalCharge(), new BigDecimal("8.95"));


        toolCode = "JAKR";
        typeCode = "JACKHAMMER"; // charges apply on weekdays only, test holiday
        dailyCharge = new BigDecimal("2.99");
        rentalDays = 7;
        checkoutDate = LocalDate.of(2024, 3, 17); // Sunday so 5 charge days

        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        when(mockToolService.getToolByCode(toolCode)).thenReturn(Optional.of(new Tool(toolCode, typeCode, "Ridgid")));
        when(mockToolService.getToolTypeByTool(any(Tool.class))).thenReturn(Optional.of(new ToolType(typeCode, "Jackhammer", dailyCharge, true, false, true)));

        // Execute
        agreement = checkoutService.toolRentalCheckout(toolCode, checkoutDate, rentalDays, discountPercent);

        // Verify
        assertNotNull(agreement);
        assertEquals(agreement.dailyRentalCharge(), dailyCharge);
        assertEquals(agreement.chargeDays(), 5);
        assertEquals(agreement.preDiscountCharge(), new BigDecimal("14.95"));
        assertEquals(agreement.discountAmount(), new BigDecimal("1.50"));
        assertEquals(agreement.finalCharge(), new BigDecimal("13.45"));


    }

    /**
     * Test generation of receipt ID
     */
    @Test
    void testReceiptIdGeneration() {
        long timestampMs = 1711154921145L; // LU3DGJAX
        int storeId = 44027; // 33*36^2+34*36+35 = XYZ
        int terminalId = 371; // = 10*36+11 = AB

        assertEquals(checkoutService.generateReceiptId(timestampMs, storeId, terminalId), "LU3DGJAX-XYZAB");
    }

    /**
     * Test that the checkout service correctly handles an invalid rental days (<0)
     */
    @Test
    void toolRentalCheckoutWithInvalidRentalDays() {
        String toolCode = "LADW";
        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        Exception exception = assertThrows(InvalidRentalDaysException.class, () ->
                checkoutService.toolRentalCheckout(toolCode, LocalDate.now(), -1, 10));
        assertTrue(exception.getMessage().contains("Rental days must be greater than zero"));
    }

    /**
     * Test that the checkout service correctly handles an invalid discount percent (<0)
     */
    @Test
    void toolRentalCheckoutWithInvalidDiscountPercent() {
        String toolCode = "LADW";
        when(mockToolService.toolExists(toolCode)).thenReturn(true);
        Exception exception = assertThrows(InvalidDiscountPercentException.class, () ->
                checkoutService.toolRentalCheckout(toolCode, LocalDate.now(), 5, -5));

        assertTrue(exception.getMessage().contains("Discount percent must be between 0 and 100"));
    }



}
