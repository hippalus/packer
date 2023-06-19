package com.mobiquity.packer.model;

import java.math.BigDecimal;
import java.util.List;

public record InboundPackage(int weightLimit, List<Item> items) {

    public List<Item> filteredItems() {
        return this.items.stream()
                .filter(item -> item.weight().compareTo(BigDecimal.valueOf(this.weightLimit)) <= 0)
                .toList();
    }
}