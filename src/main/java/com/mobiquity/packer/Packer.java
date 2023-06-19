package com.mobiquity.packer;

import com.mobiquity.exception.APIException;
import com.mobiquity.packer.model.InboundPackage;
import com.mobiquity.packer.model.OptimizedPackage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;


public final class Packer {

    private Packer() {
    }

    public static String pack(final String filePath) throws APIException {
        try {
            return pack(toPackageListFrom(filePath));
        } catch (final Exception e) {
            throw new APIException("Exception has been occurred while processing the file" + filePath, e);
        }
    }

    public static String pack(final List<InboundPackage> inboundPackages) {
        final PackageSolver packageSolver = PackageSolver.getDefaultInstance();

        return inboundPackages.stream()
                .map(packageSolver::solve)
                .map(OptimizedPackage::toText)
                .collect(Collectors.joining("\n"));
    }


    private static List<InboundPackage> toPackageListFrom(final String filename) throws IOException {
        try (final Stream<String> lines = Files.lines(Path.of(filename))) {

            final InputParser inputParser = InputParser.getDefaultInstance();

            return lines.filter(Objects::nonNull)
                    .map(inputParser::parse)
                    .toList();
        }
    }
}
