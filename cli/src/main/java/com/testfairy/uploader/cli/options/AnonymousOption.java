package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class AnonymousOption implements OptionsArg {
	private OptionSpec<Void> anonymous;

	@Override
	public void configure(OptionParser parser) {
		anonymous = parser.accepts("anonymous", "When using this option, sessions are anonymous and account information is not collected from device");
	}

	@Override
	public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
		if (!optionSet.has(anonymous))
			return builder;

		return builder == null ? new Options.Builder().setAnonymous(true) : builder.setAnonymous(true);
	}
}
