package com.panthorstudios.toolrental.api.domain;

import com.panthorstudios.toolrental.cli.adapter.OutputAdapter;

import java.math.BigDecimal;
import java.time.LocalDate;

import static com.panthorstudios.toolrental.util.FormattingTools.*;

public record RentalAgreement(String rentalAgreementId,
                              String toolCode,
                              String toolType,
                              String toolBrand,
                              int rentalDays,
                              LocalDate checkoutDate,
                              int discountPercent,
                              LocalDate dueDate,
                              int chargeDays,
                              BigDecimal dailyRentalCharge,
                              BigDecimal preDiscountCharge,
                              BigDecimal discountAmount,
                              BigDecimal finalCharge) {

    /**
     * Print the rental agreement to a string
     * @return the rental agreement as a string
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Tool code: ").append(this.toolCode()).append("\n")
                .append("Tool type: ").append(this.toolType()).append("\n")
                .append("Tool brand: ").append(this.toolBrand()).append("\n")
                .append("Rental days: ").append(formatIntegerWithCommas(this.rentalDays())).append("\n")
                .append("Check out date: ").append(this.checkoutDate().format(SHORT_DATE_FORMATTER)).append("\n")
                .append("Due date: ").append(this.dueDate().format(SHORT_DATE_FORMATTER)).append("\n")
                .append("Daily rental charge: ").append(formatMoney(this.dailyRentalCharge())).append("\n")
                .append("Charge days: ").append(formatIntegerWithCommas(this.chargeDays())).append("\n")
                .append("Pre-discount charge: ").append(formatMoney(this.preDiscountCharge())).append("\n")
                .append("Discount percent: ").append(formatPercentage(this.discountPercent())).append("\n")
                .append("Discount amount: ").append(formatMoney(this.discountAmount())).append("\n")
                .append("Final charge: ").append(formatMoney(this.finalCharge())).append("\n");

        return sb.toString();
    }
    /**
     * Print the rental agreement an output service (e.g. the console)
     */
    public void print(OutputAdapter outputAdapter) {
        outputAdapter.printLine(this.toString());
    }
}
