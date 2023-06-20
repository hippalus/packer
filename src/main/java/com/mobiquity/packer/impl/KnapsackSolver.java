package com.mobiquity.packer.impl;

import com.mobiquity.packer.PackageSolver;
import com.mobiquity.packer.model.InboundPackage;
import com.mobiquity.packer.model.Item;
import com.mobiquity.packer.model.OptimizedPackage;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class KnapsackSolver implements PackageSolver {

    public static PackageSolver getInstance() {
        return new KnapsackSolver();
    }

    KnapsackSolver() {
    }

    @Override
    public OptimizedPackage solve(final InboundPackage inboundPackage) {
        final int weightLimit = inboundPackage.weightLimit();
        final List<Item> items = inboundPackage.filteredItems();

        final BigDecimal[][] dp = this.calculateDPTable(items, weightLimit);

        final List<Item> selectedItems = this.findSelectedItems(items, dp);

        return new OptimizedPackage(weightLimit, selectedItems);
    }

    private BigDecimal[][] calculateDPTable(final List<Item> items, final int weightLimit) {
        final int itemSize = items.size();
        final BigDecimal[][] dp = new BigDecimal[itemSize + 1][weightLimit + 1];

        for (int i = 0; i <= itemSize; i++) {
            for (int w = 0; w <= weightLimit; w++) {
                if (i == 0 || w == 0) {
                    dp[i][w] = BigDecimal.ZERO;
                } else {
                    final Item currentItem = items.get(i - 1);
                    if (currentItem.weight().compareTo(BigDecimal.valueOf(w)) <= 0) {
                        final BigDecimal costWithItem = currentItem.cost().add(dp[i - 1][w - currentItem.weight().intValue()]);
                        final BigDecimal costWithoutItem = dp[i - 1][w];
                        dp[i][w] = costWithItem.max(costWithoutItem);
                    } else {
                        dp[i][w] = dp[i - 1][w];
                    }
                }
            }
        }

        return dp;
    }

    private List<Item> findSelectedItems(final List<Item> items, final BigDecimal[][] dp) {
        final List<Item> selectedItems = new ArrayList<>();
        int i = items.size();
        int w = dp[0].length - 1;

        while (i > 0 && w > 0) {
            final BigDecimal currentCell = dp[i][w];
            final BigDecimal prevCell = dp[i - 1][w];

            if (currentCell.compareTo(prevCell) != 0 || (currentCell.compareTo(prevCell) == 0 && this.isLeastWeightAmongTheSameCostsItems(items, i))) {
                selectedItems.add(items.get(i - 1));
                w -= items.get(i - 1).weight().intValue();
            }

            i--;
        }

        Collections.sort(selectedItems);

        return selectedItems;
    }

    private boolean isLeastWeightAmongTheSameCostsItems(final List<Item> items, final int currentIndex) {
        if (currentIndex >= 2) {
            final Item currentItem = items.get(currentIndex - 1);
            final Item prevItem = items.get(currentIndex - 2);
            return currentItem.cost().compareTo(prevItem.cost()) == 0 && currentItem.weight().compareTo(prevItem.weight()) <= 0;
        }
        return false;
    }
}

