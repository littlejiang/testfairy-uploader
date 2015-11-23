package com.testfairy.uploader.cli;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

class NotifyOption implements OptionsArg {
    private OptionSpec<Void> notify;

    @Override
    public void configure(OptionParser parser) {
        notify = parser.accepts("notify");
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        boolean notify = optionSet.has(this.notify);
        return builder == null ? new Options.Builder().notifyTesters(notify) : builder.notifyTesters(notify);
    }
}
