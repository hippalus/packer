package com.mobiquity.packer.model;

import java.math.BigDecimal;

public record Item(int index, BigDecimal weight, BigDecimal cost) implements Comparable<Item> {

    @Override
    public int compareTo(final Item o) {
        return Integer.compare(this.index, o.index());
    }
}