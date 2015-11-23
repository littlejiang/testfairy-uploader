package com.testfairy.uploader.cli;

import com.testfairy.uploader.Options;
import joptsimple.ArgumentAcceptingOptionSpec;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

class CommentOption implements OptionsArg {
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
        if (builder == null)
            return new Options.Builder().setComment(value);

        return builder.setComment(value);
    }
}
