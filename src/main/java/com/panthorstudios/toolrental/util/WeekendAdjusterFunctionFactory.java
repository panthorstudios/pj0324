package com.panthorstudios.toolrental.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.function.Function;

public class WeekendAdjusterFunctionFactory {

    /**
     *
     * @param weekendFunctionCode
     * @return
     */
    public static Function<LocalDate, LocalDate> getAdjusterFunction(String weekendFunctionCode) {
        return switch (weekendFunctionCode) {
            case "ADJUST_WEEKEND_TO_CLOSEST_WEEKDAY" -> adjustWeekendToClosestWeekday();
            case "ADJUST_WEEKEND_TO_MONDAY" -> adjustWeekendToMonday();
            case "ADJUST_WEEKEND_TO_FRIDAY" -> adjustWeekendToFriday();
            default -> null;
        };
    }

    /**
     * Returns a function that adjusts a date to the closest weekday
     * If the date is on a Saturday, it is adjusted to the preceding Friday
     * If the date is on a Sunday, it is adjusted to the following Monday
     * @return A function that adjusts a date to the closest weekday
     */
    private static Function<LocalDate, LocalDate> adjustWeekendToClosestWeekday() {
        return date -> {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY) {
                return date.minusDays(1); // Friday
            } else if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return date.plusDays(1); // Monday
            }
            return date;
        };
    }

    /**
        * Returns a function that adjusts a date to the following Monday
        * If the date is on a Saturday, it is adjusted to the following Monday
        * If the date is on a Sunday, it is adjusted to the following Monday
        * @return A function that adjusts a date to the following Monday
        */
    private static Function<LocalDate, LocalDate> adjustWeekendToMonday() {
        return date -> {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return date.with(TemporalAdjusters.next(DayOfWeek.MONDAY));
            }
            return date;
        };
    }

    /**
        * Returns a function that adjusts a date to the preceding Friday
        * If the date is on a Saturday, it is adjusted to the preceding Friday
        * If the date is on a Sunday, it is adjusted to the preceding Friday
        * @return A function that adjusts a date to the preceding Friday
        */
    private static Function<LocalDate, LocalDate> adjustWeekendToFriday() {
        return date -> {
            if (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
                return date.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
            }
            return date;
        };
    }
}
