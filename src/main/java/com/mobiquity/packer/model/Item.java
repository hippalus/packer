package com.mobiquity.packer.model;

import java.math.BigDecimal;

public record Item(int index, BigDecimal weight, BigDecimal cost) {
}