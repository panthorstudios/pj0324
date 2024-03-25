package com.panthorstudios.toolrental.api.service;

import com.panthorstudios.toolrental.properties.AppProperties;
import com.panthorstudios.toolrental.api.domain.HolidayRule;
import com.panthorstudios.toolrental.util.WeekendAdjusterFunctionFactory;
import org.springframework.stereotype.Service;

import java.time.DateTimeException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.function.Function;

@Service
public class HolidayService {

    private final AppProperties appProperties;

    public HolidayService(AppProperties appProperties) {
        this.appProperties = appProperties;
    }

    /**
     * Returns a set of holidays for the given range of years
     * @param startYear The start year of the range
     * @param endYear The end year of the range
     * @return A set of holidays
     */
    public Set<LocalDate> getHolidays(int startYear, int endYear) {
        Set<LocalDate> holidays = new LinkedHashSet<>();
        for (int year = startYear; year <= endYear; year++) {
            for (HolidayRule rule : appProperties.getHolidayRulesList()) {
                holidays.add(calculateHolidayDate(year, rule));
            }
        }
        return holidays;
    }

    /**
     * Calculates the date of a holiday based on the given rule
     * @param year The year to calculate the holiday date for
     * @param rule The rule that defines the holiday
     * @return The date of the holiday
     */
    protected LocalDate calculateHolidayDate(int year, HolidayRule rule) {
        if ("FIXED_DATE".equals(rule.type())) { // Fixed-date holiday
            Function<LocalDate, LocalDate> weekendAdjusterFunction = WeekendAdjusterFunctionFactory.getAdjusterFunction(rule.weekendFunctionCode());
            if (weekendAdjusterFunction == null) {
                throw new IllegalArgumentException("Invalid weekend function code: " + rule.weekendFunctionCode());
            }
            return weekendAdjusterFunction.apply(LocalDate.of(year, rule.month(), rule.day()));

        } else if ("FIXED_WEEKDAY".equals(rule.type())) { // Same weekday every year (e.g. first monday of Sept, Labor Day)
            DayOfWeek dow;
            try {
                dow = DayOfWeek.of(rule.dayOfWeek());
            } catch (DateTimeException e) {
                throw new IllegalArgumentException("Invalid day of week: " + rule.dayOfWeek());
            }
           return LocalDate.of(year, rule.month(), 1)
                    .with(TemporalAdjusters.dayOfWeekInMonth(rule.occurrence(), dow));
        }
        return null;
    }
}