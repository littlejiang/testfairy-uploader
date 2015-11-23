package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import com.testfairy.uploader.cli.StringUtils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;

public class CommentOption implements OptionsArg {
	private OptionSpec<String> comment;

	@Override
	public void configure(OptionParser parser) {
		comment = parser.accepts("comment", "Additional release notes for this upload. This text will be added to email notifications").withRequiredArg();
	}

	@Override
	public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
		if (! optionSet.has(comment))
			return builder;

		String value = optionSet.valueOf(comment);
		if (StringUtils.isEmpty(value))
			return builder;


		return addComment(builder, value);
	}

	private Options.Builder addComment(Options.Builder builder, String value) {
		if (builder == null)
			return new Options.Builder().setComment(value);

		return builder.setComment(value);
	}
}
