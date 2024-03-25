package com.panthorstudios.toolrental.util;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.format.DateTimeFormatter;

public class FormattingTools {

    // Constants for formatting
    public static final String US_MONEY_FORMAT = "$%,.2f";

    public static final String INTEGER_COMMA_FORMAT = "%,d";

    public static final String INTEGER_PERCENTAGE_FORMAT = "%d%%";

    public static final DateTimeFormatter SHORT_DATE_FORMATTER = DateTimeFormatter.ofPattern("MM/dd/yy");

    // format a BigDecimal as US currency
    public static String formatMoney(BigDecimal value) {
        return String.format(US_MONEY_FORMAT, value.setScale(2, RoundingMode.UP));
    }

    // format an int as a percentage
    public static String formatPercentage(int value) {
        return String.format(INTEGER_PERCENTAGE_FORMAT, value);
    }

    // format an int with commas
    public static String formatIntegerWithCommas(int value) {
        return String.format(INTEGER_COMMA_FORMAT, value);
    }
}
