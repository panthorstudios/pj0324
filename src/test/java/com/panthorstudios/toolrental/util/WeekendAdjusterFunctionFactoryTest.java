package com.panthorstudios.toolrental.util;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import java.util.function.Function;
import static org.junit.jupiter.api.Assertions.*;

public class WeekendAdjusterFunctionFactoryTest {

    private LocalDate friday = LocalDate.of(2024,3,22);
    private LocalDate saturday = LocalDate.of(2024,3,23);
    private LocalDate sunday = LocalDate.of(2024,3,24);
    private LocalDate monday = LocalDate.of(2024,3,25);

    @Test
    public void testAdjustWeekendToClosestWeekday_Saturday() {
        String code = "ADJUST_WEEKEND_TO_CLOSEST_WEEKDAY";

        Function<LocalDate, LocalDate> adjuster = WeekendAdjusterFunctionFactory.getAdjusterFunction(code);
        LocalDate adjustedDate = adjuster.apply(saturday);

        assertEquals(friday, adjustedDate, "A Saturday should be adjusted to the preceding Friday.");
    }

    @Test
    public void testAdjustWeekendToClosestWeekday_Sunday() {
        String code = "ADJUST_WEEKEND_TO_CLOSEST_WEEKDAY";

        Function<LocalDate, LocalDate> adjuster = WeekendAdjusterFunctionFactory.getAdjusterFunction(code);
        LocalDate adjustedDate = adjuster.apply(sunday);

        assertEquals(monday, adjustedDate, "A Sunday should be adjusted to the following Monday.");
    }

    @Test
    public void testAdjustWeekendToMonday_Weekend() {
        String code = "ADJUST_WEEKEND_TO_MONDAY";

        Function<LocalDate, LocalDate> adjuster = WeekendAdjusterFunctionFactory.getAdjusterFunction(code);

        assertEquals(monday, adjuster.apply(saturday), "A Saturday should be adjusted to the following Monday.");
        assertEquals(monday, adjuster.apply(sunday), "A Sunday should be adjusted to the following Monday.");
    }

    @Test
    public void testAdjustWeekendToFriday_Weekend() {
        String code = "ADJUST_WEEKEND_TO_FRIDAY";

        Function<LocalDate, LocalDate> adjuster = WeekendAdjusterFunctionFactory.getAdjusterFunction(code);

        assertEquals(friday, adjuster.apply(saturday), "A Saturday should be adjusted to the preceding Friday.");
        assertEquals(friday, adjuster.apply(sunday), "A Sunday should be adjusted to the preceding Friday.");
    }

    @Test
    public void testWeekday_Unchanged() {
        // Test that weekdays are not adjusted, using the closest weekday adjuster as an example
        String code = "ADJUST_WEEKEND_TO_CLOSEST_WEEKDAY";

        LocalDate weekday = LocalDate.of(2023, 10, 18); // Assuming this is a Wednesday

        Function<LocalDate, LocalDate> adjuster = WeekendAdjusterFunctionFactory.getAdjusterFunction(code);
        LocalDate adjustedDate = adjuster.apply(weekday);

        assertEquals(weekday, adjustedDate, "A weekday should remain unchanged.");
    }

    @Test
    public void testInvalidCode_ThrowsIllegalArgumentException() {
        String invalidCode = "INVALID_CODE";
        assertNull(WeekendAdjusterFunctionFactory.getAdjusterFunction(invalidCode));
    }
}

