package com.mobiquity.packer.model;

import java.util.List;

public record InboundPackage(int weightLimit, List<Item> items) {
}