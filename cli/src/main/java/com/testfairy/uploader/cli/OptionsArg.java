package com.testfairy.uploader.cli;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

interface OptionsArg {
    void configure(OptionParser parser);
    Options.Builder apply(OptionSet optionSet, Options.Builder builder);
}
