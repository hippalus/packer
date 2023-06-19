package com.mobiquity.packer.impl;

import com.mobiquity.exception.PackerValidationException;
import com.mobiquity.packer.InputValidator;

import java.math.BigDecimal;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class TextInputValidator implements InputValidator {
    private static final int MAX_ITEMS = 15;
    private static final int MAX_PACKAGE_WEIGHT = 100;
    private static final BigDecimal MAX_ITEM_WEIGHT = new BigDecimal(100);
    private static final BigDecimal MAX_ITEM_COST = new BigDecimal(100);
    private static final Pattern ITEM_PATTERN = Pattern.compile("\\((\\d+),(\\d+(\\.\\d+)?),€(\\d+)\\)");

    private static Pattern prepareLinePattern(final int maxItems) {
        return Pattern.compile("^(\\d+)\\s*:\\s*((\\(\\d+,\\d+(\\.\\d+)?,€\\d+\\)\\s*){1," + maxItems + "})$");
    }

    public static InputValidator getDefaultInstance() {
        return new TextInputValidator(MAX_ITEMS, MAX_PACKAGE_WEIGHT, MAX_ITEM_WEIGHT, MAX_ITEM_COST);
    }

    private final int maxItems;
    private final int maxPackageWeight;
    private final BigDecimal maxItemWeight;
    private final BigDecimal maxItemCost;
    private final Pattern itemPattern;
    private final Pattern linePattern;

    TextInputValidator(final int maxItems, final int maxPackageWeight, final BigDecimal maxItemWeight, final BigDecimal maxItemCost) {
        this(maxItems, maxPackageWeight, maxItemWeight, maxItemCost, ITEM_PATTERN, prepareLinePattern(maxItems));
    }

    @SuppressWarnings("ConstructorWithTooManyParameters")
    TextInputValidator(final int maxItems,
                       final int maxPackageWeight,
                       final BigDecimal maxItemWeight,
                       final BigDecimal maxItemCost,
                       final Pattern itemPattern,
                       final Pattern linePattern) {
        this.maxItems = maxItems;
        this.maxPackageWeight = maxPackageWeight;
        this.maxItemWeight = maxItemWeight;
        this.maxItemCost = maxItemCost;
        this.itemPattern = itemPattern;
        this.linePattern = linePattern;
    }


    @Override
    public Matcher createPackageLineMatcher(final String line) {
        final Matcher lineMatcher = this.linePattern.matcher(line);
        if (!lineMatcher.matches()) {
            throw new PackerValidationException("Invalid line format or too many items. Maximum allowed is " + this.maxItems + " . " + line);
        }
        return lineMatcher;
    }

    @Override
    public Matcher createItemLineMatcher(final String items) {
        return this.itemPattern.matcher(items);
    }

    @Override
    public void validatePackageWeightLimit(final int weight) {
        if (weight <= 0 || weight > this.maxPackageWeight) {
            throw new PackerValidationException("Invalid weight for package: " + weight);
        }
    }

    @Override
    public void validateItemWeightLimit(final BigDecimal weight, final int itemIndex) {
        if (weight.compareTo(BigDecimal.ZERO) <= 0 || weight.compareTo(this.maxItemWeight) > 0) {
            throw new PackerValidationException("Invalid weight for item " + itemIndex + ": " + weight);
        }
    }

    @Override
    public void validateItemCost(final BigDecimal cost, final int itemIndex) {
        if (cost.compareTo(BigDecimal.ZERO) <= 0 || cost.compareTo(this.maxItemCost) > 0) {
            throw new PackerValidationException("Invalid cost for item " + itemIndex + ": " + cost);
        }
    }
}