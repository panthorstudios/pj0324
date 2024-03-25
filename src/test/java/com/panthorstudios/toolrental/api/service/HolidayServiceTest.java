package com.panthorstudios.toolrental.api.service;

import com.panthorstudios.toolrental.properties.AppProperties;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.Set;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(properties = "app.mode=web")

class HolidayServiceTest {

    private HolidayService holidayService;

    @Autowired
    private AppProperties appProperties;

    private LocalDate friday = LocalDate.of(2024,3,22);

    private LocalDate saturday = LocalDate.of(2024,3,23);
    private LocalDate sunday = LocalDate.of(2024,3,24);
    private LocalDate monday = LocalDate.of(2024,3,25);

    @BeforeEach
    void setUp() {
        holidayService = new HolidayService(appProperties);
    }

    @Test
    void getHolidays_includesIndependenceDay() {
        int year = 2022; // Choosing a year when July 4 is a Monday
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate expectedJuly4 = LocalDate.of(year, 7, 4);
        assertTrue(holidays.contains(expectedJuly4), "July 4 should be included as a holiday.");
    }

    @Test
    void testIndependenceDayObserved_onFridayIfSaturday() {
        int year = 2020; // July 4, 2020, is a Saturday
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate observedJuly4 = LocalDate.of(year, 7, 3); // Observed on Friday, July 3, 2020
        assertTrue(holidays.contains(observedJuly4), "Independence Day should be observed on Friday if it falls on Saturday.");
    }
    @Test
    void testIndependenceDayObserved_onThursday() {
        int year = 2024; // July 4, 2024, is a Thursday
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate observedJuly4 = LocalDate.of(year, 7, 4); // Observed on Thursday, July 4, 2024
        assertTrue(holidays.contains(observedJuly4), "Independence Day should be observed on Thursday.");
    }
    @Test
    void testLaborDayObserved_onMonday() {
        int year = 2024; // July 4, 2024, is a Thursday
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate observedLaborDay = LocalDate.of(year, 9, 2); // Observed on Monday, Sept 2, 2024
        assertTrue(holidays.contains(observedLaborDay), "Independence Day should be observed on Monday.");
    }
    @Test
    void testIndependenceDayObserved_onMondayIfSunday() {
        int year = 2021; // July 4, 2021, is a Sunday
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate observedJuly4 = LocalDate.of(year, 7, 5); // Observed on Monday, July 5, 2021
        assertTrue(holidays.contains(observedJuly4), "Independence Day should be observed on Monday if it falls on Sunday.");
    }

    @Test
    void testGetHolidays_includesLaborDay() {
        int year = 2022; // To test a specific year's Labor Day
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate laborDay = LocalDate.of(year, 9, 1).with(java.time.temporal.TemporalAdjusters.firstInMonth(java.time.DayOfWeek.MONDAY));
        assertTrue(holidays.contains(laborDay), "Labor Day should be included as a holiday.");
    }

    @Test
    void testLaborDay_isFirstMondayOfSeptember() {
        int year = 2023; // Just choosing a year for example
        Set<LocalDate> holidays = holidayService.getHolidays(year, year);
        LocalDate expectedLaborDay = LocalDate.of(year, 9, 4); // First Monday of September 2023
        assertTrue(holidays.contains(expectedLaborDay), "Labor Day should be the first Monday of September.");
    }
    /**
     * Test the calculateHolidayDate method with a holiday rule that specifies a fixed date.
     */
    @Test
    void testCalculateHolidayDateJuly4() {
        assertEquals(LocalDate.of(2021,7,5), holidayService.calculateHolidayDate(2021, appProperties.getHolidayRulesList().get(0)));
    }

    /**
        * Test the calculateHolidayDate method with a holiday rule that specifies a date based on a day of the week.
     */
    @Test
    void testCalculateHolidayDateLaborDay() {
        assertEquals(LocalDate.of(2024,9,2), holidayService.calculateHolidayDate(2024, appProperties.getHolidayRulesList().get(1)));
    }

    /**
     * Test the adjustWeekendToClosestWeekday method with a Sunday date.
     */
    @Test
    void testAdjustWeekendToClosestWeekdaySunday() {
        Function<LocalDate,LocalDate> adjustFunction = holidayService.adjustWeekendToClosestWeekday();
        assertEquals(monday, adjustFunction.apply(sunday));
    }
    /**
     * Test the adjustWeekendToClosestWeekday method with a Saturday date.
     */
    @Test
    void testAdjustWeekendToClosestWeekdaySaturday() {
        Function<LocalDate,LocalDate> adjustFunction = holidayService.adjustWeekendToClosestWeekday();
        assertEquals(friday, adjustFunction.apply(saturday));
    }
    /**
     * Test the adjustWeekendToFriday method with a Saturday date.
     */
    @Test
    void testAdjustWeekendToFridaySaturday() {
        Function<LocalDate,LocalDate> adjustFunction = holidayService.adjustWeekendToFriday();
        assertEquals(friday, adjustFunction.apply(saturday));
    }
    /**
     * Test the adjustWeekendToFriday method with a Sunday date.
     */
    @Test
    void testAdjustWeekendToFridaySunday() {
        Function<LocalDate,LocalDate> adjustFunction = holidayService.adjustWeekendToFriday();
        assertEquals(friday, adjustFunction.apply(sunday));
    }
    /**
     * Test the adjustWeekendToMonday method with a Sunday date.
     */
    @Test
    void testAdjustWeekendToMondaySunday() {
        Function<LocalDate,LocalDate> adjustFunction = holidayService.adjustWeekendToMonday();
        assertEquals(monday, adjustFunction.apply(sunday));
    }
    @Test
    void testAdjustWeekendToMondaySaturday() {
        Function<LocalDate,LocalDate> adjustFunction = holidayService.adjustWeekendToMonday();
        assertEquals(monday, adjustFunction.apply(saturday));
    }
}
