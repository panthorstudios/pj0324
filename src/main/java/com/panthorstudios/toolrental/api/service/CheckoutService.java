package com.panthorstudios.toolrental.api.service;

import com.panthorstudios.toolrental.api.domain.RentalAgreement;
import com.panthorstudios.toolrental.api.domain.Tool;
import com.panthorstudios.toolrental.api.domain.ToolType;
import com.panthorstudios.toolrental.api.exception.InvalidCheckoutDateException;
import com.panthorstudios.toolrental.api.exception.InvalidDiscountPercentException;
import com.panthorstudios.toolrental.api.exception.InvalidRentalDaysException;
import com.panthorstudios.toolrental.api.exception.InvalidToolCodeException;
import com.panthorstudios.toolrental.properties.AppProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Set;

@Service
public class CheckoutService {
    // This is a record class that represents the charge details for a tool rental
    private static final Logger logger = LoggerFactory.getLogger(CheckoutService.class);

    public record ChargeDetails (BigDecimal charge, int days) {}

    private final ToolService toolService;
    private final HolidayService holidayService;

    private final AppProperties appProperties;

    public CheckoutService(ToolService toolService, HolidayService holidayService, AppProperties appProperties) {
        this.toolService = toolService;
        this.holidayService = holidayService;
        this.appProperties =  appProperties;
    }

    /** Checks out a tool rental.
     *
     * @param toolCode the tool code
     * @param checkoutDate the checkout date
     * @param rentalDays the rental days
     * @param discountPercent the discount percent
     * @return the rental agreement
     * @throws InvalidToolCodeException if any of the parameters are invalid
     */
    public RentalAgreement toolRentalCheckout(String toolCode, LocalDate checkoutDate, int rentalDays, int discountPercent) {
        logger.debug("Checking out tool rental for tool: {}, checkoutDate: {}", toolCode, checkoutDate);
        validateCheckoutParameters(toolCode, checkoutDate, rentalDays, discountPercent);

        Tool tool = getToolByCode(toolCode);
        ToolType toolType = getToolTypeByTool(tool);
        LocalDate dueDate = checkoutDate.plusDays(rentalDays);

        Set<LocalDate> holidays = holidayService.getHolidays(checkoutDate.getYear(), dueDate.getYear());
        ChargeDetails chargeDetails = calculateCharges(toolType, checkoutDate, rentalDays, holidays);

        BigDecimal discountAmount = calculateDiscountAmount(chargeDetails.charge(), discountPercent);
        BigDecimal finalCharge = chargeDetails.charge().subtract(discountAmount);

        return createRentalAgreement(tool, toolType, rentalDays, checkoutDate, discountPercent, dueDate, chargeDetails, discountAmount, finalCharge);
    }

    private Tool getToolByCode(String toolCode) {
        return toolService.getToolByCode(toolCode)
                .orElseThrow(() -> new InvalidToolCodeException("Tool not found: " + toolCode));
    }

    private ToolType getToolTypeByTool(Tool tool) {
        return toolService.getToolTypeByTool(tool)
                .orElseThrow(() -> new InvalidToolCodeException("Tool type not found"));
    }

    private BigDecimal calculateDiscountAmount(BigDecimal charge, int discountPercent) {
        return charge.multiply(BigDecimal.valueOf(discountPercent))
                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);
    }

    private RentalAgreement createRentalAgreement(Tool tool, ToolType toolType, int rentalDays, LocalDate checkoutDate, int discountPercent, LocalDate dueDate, ChargeDetails chargeDetails, BigDecimal discountAmount, BigDecimal finalCharge) {
        logger.info("Creating rental agreement for tool: {}", tool.code());
        long timestampMs = Instant.now().toEpochMilli();
        String rentalAgreementId = generateReceiptId(timestampMs, appProperties.getStoreId(), appProperties.getTerminalId());
        return new RentalAgreement(
                rentalAgreementId,
                tool.code(),
                toolType.label(),
                tool.brand(),
                rentalDays,
                checkoutDate,
                discountPercent,
                dueDate,
                chargeDetails.days(),
                toolType.dailyCharge(),
                chargeDetails.charge(),
                discountAmount,
                finalCharge
        );
    }

    /**
     * Calculates the charges for a tool rental.
     *
     * @param toolType the tool type
     * @param checkoutDate the checkout date
     * @param rentalDays the rental days
     * @param holidays the holidays
     * @return the charge details
     */
    private ChargeDetails calculateCharges(ToolType toolType, LocalDate checkoutDate, int rentalDays, Set<LocalDate> holidays) {

        BigDecimal itemCharge = toolType.dailyCharge();
        int chargeDays = 0;
        BigDecimal preDiscountCharge = BigDecimal.ZERO;

        // Iterate through each day of the rental period
        //  starting on day after checkout date and ending on the due date
        for (int i = 1; i <= rentalDays; i++) {
            LocalDate day = checkoutDate.plusDays(i);
            boolean isHoliday = holidays.contains(day);
            boolean isWeekend = day.getDayOfWeek() == DayOfWeek.SATURDAY || day.getDayOfWeek() == DayOfWeek.SUNDAY;
            boolean isWeekday = !isHoliday && !isWeekend;
            if ((isHoliday && toolType.chargeableOnHolidays())
                    || (isWeekend && toolType.chargeableOnWeekends())
                    || (isWeekday && toolType.chargeableOnWeekdays())) {

                preDiscountCharge = preDiscountCharge.add(itemCharge);
                chargeDays++;
            }
        }
        return new ChargeDetails(preDiscountCharge, chargeDays);
    }

    /**
     * Generates a unique receipt ID based on the store ID, terminal ID, and current timestamp.
     *
     * @param storeId the store ID
     * @param terminalId the terminal ID
     * @return the receipt ID
     */
    public String generateReceiptId(long timestampMs, int storeId, int terminalId) {

        // Convert the Unix timestamp to base 36
        String timestampBase36 = toBase36(timestampMs);

        // Ensure the store ID is exactly 3 characters and terminal ID is exactly 2 characters, padded with leading zeros if necessary
        String paddedStoreId = String.format("%3s", toBase36(storeId)).replace(' ', '0');
        String paddedTerminalId = String.format("%2s", toBase36(terminalId)).replace(' ', '0');

        // Construct the receipt ID
        return String.format("%s-%s%s", timestampBase36, paddedStoreId, paddedTerminalId);
    }
    /**
     * Converts a long value to a base 36 string.
     *
     * @param value the long value
     * @return the base 36 string
     */
    private String toBase36(long value) {
        return new BigInteger(Long.toString(value)).toString(36).toUpperCase();
    }

    /**
     * Validates the checkout parameters.
     *
     * @param toolCode the tool code
     * @param checkoutDate the checkout date
     * @param rentalDays the rental days
     * @param discountPercent the discount percent
     * @throws InvalidDiscountPercentException, InvalidToolCodeException, InvalidDiscountPercentException, InvalidCheckoutDateException if any of the parameters are invalid
     */
    public void validateCheckoutParameters(String toolCode, LocalDate checkoutDate, int rentalDays, int discountPercent) throws InvalidDiscountPercentException, InvalidToolCodeException, InvalidDiscountPercentException, InvalidCheckoutDateException {
        validateToolCode(toolCode);
        validateCheckoutDate(checkoutDate);
        validateRentalDays(rentalDays);
        validateDiscountPercent(discountPercent);
    }

    /**
     * Validates the tool code.
     *
     * @param toolCode the tool code
     * @throws InvalidToolCodeException if the tool code is null or empty
     */
    public void validateToolCode(String toolCode) throws InvalidToolCodeException {
        if (toolCode == null || toolCode.isEmpty() || toolCode.isBlank()) {
            throw new InvalidToolCodeException("Tool code is required.");
        }
        if (!toolService.toolExists(toolCode)) {
            throw new InvalidToolCodeException("Tool code is not valid: " + toolCode);
        }

    }
    /**
     * Validates the checkout date.
     *
     * @param checkoutDate the checkout date
     * @throws InvalidCheckoutDateException if the checkout date is null
     */
    public void validateCheckoutDate(LocalDate checkoutDate) throws InvalidCheckoutDateException {
        if (checkoutDate == null) {
            throw new InvalidCheckoutDateException("Checkout date is required.");
        }
    }
    /** Validates the rental days.
     *
     * @param rentalDays the rental days
     * @throws InvalidRentalDaysException if the rental days is less than or equal to zero
     */
    public void validateRentalDays(int rentalDays) throws InvalidRentalDaysException {
        if (rentalDays <= 0) {
            throw new InvalidRentalDaysException("Rental days must be greater than zero.");
        }
    }

    /** Validates the discount percent.
     *
     * @param discountPercent the discount percent
     * @throws InvalidDiscountPercentException if the discount percent is less than zero or greater than 100
     */
    public void validateDiscountPercent(int discountPercent) throws InvalidDiscountPercentException {
        if (discountPercent < 0 || discountPercent > 100) {
            throw new InvalidDiscountPercentException("Discount percent must be between 0 and 100.");
        }
    }
}
