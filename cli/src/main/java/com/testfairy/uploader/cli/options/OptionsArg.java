package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;

public interface OptionsArg {
	void configure(OptionParser parser);
	Options.Builder apply(OptionSet optionSet, Options.Builder builder);
}
