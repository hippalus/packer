package com.mobiquity.packer;

import com.mobiquity.packer.impl.TextInputParser;
import com.mobiquity.packer.model.InboundPackage;

public interface InputParser {

    static InputParser getDefaultInstance() {
        return TextInputParser.newInstance(InputValidator.getInstance());
    }

    InboundPackage parse(final String input);

}
