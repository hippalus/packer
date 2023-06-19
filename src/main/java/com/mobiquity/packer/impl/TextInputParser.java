package com.mobiquity.packer.impl;

import com.mobiquity.packer.InputParser;
import com.mobiquity.packer.InputValidator;
import com.mobiquity.packer.model.InboundPackage;
import com.mobiquity.packer.model.Item;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;

public final class TextInputParser implements InputParser {

    private static final int MAX_WEIGHT_GROUP_INDEX = 1;
    private static final int ITEMS_GROUP_INDEX = 2;

    private static final int ITEM_INDEX_GROUP_INDEX = 1;
    private static final int ITEM_WEIGHT_GROUP_INDEX = 2;
    private static final int ITEM_COST_GROUP_INDEX = 4;

    public static InputParser newInstance(final InputValidator inputValidator) {
        return new TextInputParser(inputValidator);
    }

    private final InputValidator inputValidator;

    TextInputParser(final InputValidator inputValidator) {
        this.inputValidator = inputValidator;
    }

    @Override
    public InboundPackage parse(final String input) {
        final Matcher lineMatcher = this.inputValidator.createPackageLineMatcher(input);

        final int maxWeight = this.getMaxWeight(lineMatcher);
        final List<Item> items = this.getItems(lineMatcher);

        return new InboundPackage(maxWeight, items);
    }

    private int getMaxWeight(final Matcher lineMatcher) {
        final int maxWeight = Integer.parseInt(lineMatcher.group(MAX_WEIGHT_GROUP_INDEX));
        this.inputValidator.validatePackageWeightLimit(maxWeight);
        return maxWeight;
    }

    private List<Item> getItems(final Matcher lineMatcher) {
        final String itemsString = lineMatcher.group(ITEMS_GROUP_INDEX);
        final Matcher itemMatcher = this.inputValidator.createItemLineMatcher(itemsString);
        final List<Item> items = new ArrayList<>();

        while (itemMatcher.find()) {
            final int index = this.getIndex(itemMatcher);
            final BigDecimal weight = this.getItemWeight(itemMatcher, index);
            final BigDecimal cost = this.getItemCost(itemMatcher, index);

            final Item item = new Item(index, weight, cost);
            items.add(item);
        }

        return items;
    }

    private int getIndex(final Matcher itemMatcher) {
        return Integer.parseInt(itemMatcher.group(ITEM_INDEX_GROUP_INDEX));
    }

    private BigDecimal getItemWeight(final Matcher itemMatcher, final int index) {
        final BigDecimal weight = new BigDecimal(itemMatcher.group(ITEM_WEIGHT_GROUP_INDEX));
        this.inputValidator.validateItemWeightLimit(weight, index);
        return weight;
    }

    private BigDecimal getItemCost(final Matcher itemMatcher, final int index) {
        final BigDecimal cost = new BigDecimal(itemMatcher.group(ITEM_COST_GROUP_INDEX));
        this.inputValidator.validateItemCost(cost, index);
        return cost;
    }

}

