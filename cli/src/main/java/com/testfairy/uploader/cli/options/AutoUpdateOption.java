package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class AutoUpdateOption implements OptionsArg {
	private OptionSpec<Void> autoUpdate;

	@Override
	public void configure(OptionParser parser) {
		autoUpdate = parser.accepts("auto-update", "Allows easy upgrade of all users to current version.");
	}

	@Override
	public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
		if (!optionSet.has(autoUpdate))
			return builder;

		return builder == null ? new Options.Builder().setAutoUpdate(true) : builder.setAutoUpdate(true);
	}
}
