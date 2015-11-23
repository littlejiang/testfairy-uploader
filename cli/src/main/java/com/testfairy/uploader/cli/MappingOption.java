package com.testfairy.uploader.cli;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

import java.io.File;
import java.util.Arrays;

class MappingOption implements OptionsArg {
    private OptionSpec<File> mapping;

    @Override
    public void configure(OptionParser parser) {
//        mapping = parser
//            .acceptsAll(Arrays.asList("proguard-mapping", "symbols-file"))
//            .withRequiredArg()
//            .ofType(File.class);
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
//        if (! optionSet.has(mapping))
//            return builder;
//
//        return builder == null ? new Options.Builder().set;
        return builder;
    }
}
