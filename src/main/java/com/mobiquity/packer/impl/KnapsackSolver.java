package com.mobiquity.packer.impl;

import com.mobiquity.packer.PackageSolver;
import com.mobiquity.packer.model.InboundPackage;
import com.mobiquity.packer.model.Item;
import com.mobiquity.packer.model.OptimizedPackage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public final class KnapsackSolver implements PackageSolver {

    public static PackageSolver getInstance() {
        return new KnapsackSolver();
    }

    KnapsackSolver() {
    }

    @Override
    public OptimizedPackage solve(final InboundPackage inboundPackage) {

        final List<Item> items = inboundPackage.items();
        final int n = items.size();
        final int weightLimit = inboundPackage.weightLimit();
        final BigDecimal[][] dp = new BigDecimal[n + 1][weightLimit + 1];

        for (int i = 0; i <= n; i++) {
            for (int w = 0; w <= weightLimit; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = BigDecimal.ZERO;
                } else if (items.get(i - 1).weight().compareTo(BigDecimal.valueOf(w)) <= 0) {
                    final BigDecimal costWithItem = items.get(i - 1).cost().add(dp[i - 1][w - items.get(i - 1).weight().intValue()]);
                    final BigDecimal costWithoutItem = dp[i - 1][w];
                    dp[i][w] = costWithItem.max(costWithoutItem);
                } else {
                    dp[i][w] = dp[i - 1][w];
                }
            }
        }

        final List<Item> selectedItems = new ArrayList<>();
        int i = n;
        int w = weightLimit;
        while (i > 0 && w > 0) {
            if (dp[i][w].compareTo(dp[i - 1][w]) != 0) {
                selectedItems.add(items.get(i - 1));
                w -= items.get(i - 1).weight().intValue();
            }
            i--;
        }

        // Sort selected items by weight in ascending order
        selectedItems.sort(Comparator.comparing(Item::index));

        return new OptimizedPackage(inboundPackage.weightLimit(), selectedItems);

    }
}
