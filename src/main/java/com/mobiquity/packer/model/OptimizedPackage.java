package com.mobiquity.packer.model;

import java.util.List;
import java.util.stream.Collectors;

public record OptimizedPackage(int weightLimit, List<Item> items) {

    public String toText() {
        if (this.items.isEmpty()) {
            return "-";
        }

        return this.items.stream()
                .map(item -> String.valueOf(item.index()))
                .collect(Collectors.joining(","));
    }

    public List<Integer> indices() {
        return this.items.stream()
                .map(Item::index)
                .toList();
    }
}
