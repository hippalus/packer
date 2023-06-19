package com.mobiquity.packer;

import com.mobiquity.packer.impl.TextInputValidator;

import java.math.BigDecimal;
import java.util.regex.Matcher;

public interface InputValidator {

    static InputValidator getInstance() {
        return TextInputValidator.getDefaultInstance();
    }

    Matcher createPackageLineMatcher(String line);

    Matcher createItemLineMatcher(String items);

    void validatePackageWeightLimit(final int weight);

    void validateItemWeightLimit(BigDecimal weight, int itemIndex);

    void validateItemCost(BigDecimal cost, int itemIndex);
}
