package com.mobiquity.packer;

import com.mobiquity.packer.impl.KnapsackSolver;
import com.mobiquity.packer.model.InboundPackage;
import com.mobiquity.packer.model.OptimizedPackage;

public interface PackageSolver {

    static PackageSolver getDefaultInstance() {
        return KnapsackSolver.getInstance();
    }

    OptimizedPackage solve(InboundPackage inboundPackage);

}
