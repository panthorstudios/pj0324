package com.panthorstudios.toolrental.api.domain;

import com.panthorstudios.toolrental.api.service.OutputService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RentalAgreementTest {

    @Mock
    private OutputService outputService;

    /**
     * Test the toString method.
     */
    @Test
    void testToString() {
        RentalAgreement agreement = new RentalAgreement(
                "RA-001",
                "LADW",
                "Ladder",
                "Werner",
                5,
                LocalDate.of(2023, 7, 8),
                10,
                LocalDate.of(2023, 7, 13),
                5,
                new BigDecimal("2.99"),
                new BigDecimal("14.95"),
                new BigDecimal("1.50"),
                new BigDecimal("13.45")
        );

        String expectedOutput = """
                Tool code: LADW
                Tool type: Ladder
                Tool brand: Werner
                Rental days: 5
                Check out date: 07/08/23
                Due date: 07/13/23
                Daily rental charge: $2.99
                Charge days: 5
                Pre-discount charge: $14.95
                Discount percent: 10%
                Discount amount: $1.50
                Final charge: $13.45""";

        assertEquals(expectedOutput, agreement.toString().trim());
    }
    @Test
    void testToStringWithCommas() {
        RentalAgreement agreement = new RentalAgreement(
                "RA-001",
                "LADW",
                "Ladder",
                "Werner",
                5000,
                LocalDate.of(2023, 7, 8),
                10,
                LocalDate.of(2023, 7, 13),
                4000,
                new BigDecimal("2000000.99"),
                new BigDecimal("2000000.95"),
                new BigDecimal("2000000.50"),
                new BigDecimal("2000000.45")
        );

        String expectedOutput = """
                Tool code: LADW
                Tool type: Ladder
                Tool brand: Werner
                Rental days: 5,000
                Check out date: 07/08/23
                Due date: 07/13/23
                Daily rental charge: $2,000,000.99
                Charge days: 4,000
                Pre-discount charge: $2,000,000.95
                Discount percent: 10%
                Discount amount: $2,000,000.50
                Final charge: $2,000,000.45""";

        assertEquals(expectedOutput, agreement.toString().trim());
    }

    /**
     * Test the print method.
     */
    @Test
    void testPrint() {
        RentalAgreement agreement = new RentalAgreement(
                "RA-002",
                "CHNS",
                "Chainsaw",
                "Stihl",
                3,
                LocalDate.of(2023, 7, 8),
                15,
                LocalDate.of(2023, 7, 11),
                3,
                new BigDecimal("1.49"),
                new BigDecimal("4.47"),
                new BigDecimal("0.67"),
                new BigDecimal("3.80")
        );

        agreement.print(outputService);

        // Verify that outputService.printLine() is called with the expected string
        verify(outputService).printLine(agreement.toString());
    }
}
