package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.exception.PackerValidationException;
import com.mobiquity.packer.model.InboundPackage;
import com.mobiquity.packer.model.Item;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PackerTest {
    private final InputParser inputParser = InputParser.getDefaultInstance();


    @Test
    void parseItems_ValidInput_ReturnsParsedItems() throws APIException {
        final String packageLine = "56 : (1,90.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)";
        final List<Item> expectedItems = List.of(
                new Item(1, new BigDecimal("90.72"), new BigDecimal("13")),
                new Item(2, new BigDecimal("33.80"), new BigDecimal("40")),
                new Item(3, new BigDecimal("43.15"), new BigDecimal("10")),
                new Item(4, new BigDecimal("37.97"), new BigDecimal("16")),
                new Item(5, new BigDecimal("46.81"), new BigDecimal("36")),
                new Item(6, new BigDecimal("48.77"), new BigDecimal("79")),
                new Item(7, new BigDecimal("81.80"), new BigDecimal("45")),
                new Item(8, new BigDecimal("19.36"), new BigDecimal("79")),
                new Item(9, new BigDecimal("6.76"), new BigDecimal("64"))
        );

        final InboundPackage parsedItems = this.inputParser.parse(packageLine);

        assertEquals(56, parsedItems.weightLimit());
        assertEquals(expectedItems, parsedItems.items());
    }


    @ParameterizedTest
    @MethodSource("invalidDataAndExceptionTestCaseInputProvider")
    void parseItems_InValidInput_ThrowsAPIException(final String invalidLine, final String exceptionDetail) {

        final String message = Assertions.assertThrowsExactly(
                        PackerValidationException.class,
                        () -> this.inputParser.parse(invalidLine))
                .getMessage();

        assertEquals(exceptionDetail, message);
    }


    public static Stream<Arguments> invalidDataAndExceptionTestCaseInputProvider() {

        return Stream.of(
                Arguments.of(
                        "120 : (1,50.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)",
                        "Invalid weight for package: 120"
                ),
                Arguments.of(
                        "56 : (1,150.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)",
                        "Invalid weight for item 1: 150.72"
                ),
                Arguments.of(
                        "56 : (1,50.72,€130) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64)",
                        "Invalid cost for item 1: 130"
                ),
                Arguments.of(
                        "56 : (1,10.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) (5,46.81,€36) (6,48.77,€79) (7,81.80,€45)" +
                                " (8,19.36,€79) (9,6.76,€64) (10,10.72,€13) (11,33.80,€40) (12,15.15,€10) (13,37.97,€16) (14,46.81,€36) (15,48.77,€79) (16,81.80,€45) (17,19.36,€79) (18,6.76,€64)",
                        "Invalid line format or too many items. Maximum allowed is 15 . 56 : (1,10.72,€13) (2,33.80,€40) (3,43.15,€10) (4,37.97,€16) " +
                                "(5,46.81,€36) (6,48.77,€79) (7,81.80,€45) (8,19.36,€79) (9,6.76,€64) (10,10.72,€13) (11,33.80,€40) (12,15.15,€10) (13,37.97,€16) " +
                                "(14,46.81,€36) (15,48.77,€79) (16,81.80,€45) (17,19.36,€79) (18,6.76,€64)"
                )
        );
    }
}