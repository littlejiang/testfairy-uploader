package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class MaxDurationOption implements OptionsArg {
	private OptionSpec<String> maxDuration;

	@Override
	public void configure(OptionParser parser) {
		maxDuration = parser.accepts("max-duration", "Maximum session recording length, eg 20m or 1h. Default is \"10m\". Maximum 24h").withRequiredArg();
	}

	@Override
	public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
		if (!optionSet.has(maxDuration))
			return builder;

		String maxDuration = optionSet.valueOf(this.maxDuration);
		return builder == null ? new Options.Builder().setMaxDuration(maxDuration) : builder.setMaxDuration(maxDuration);
	}
}
