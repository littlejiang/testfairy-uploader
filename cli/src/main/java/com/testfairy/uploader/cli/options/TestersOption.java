package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import com.testfairy.uploader.cli.StringUtils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class TestersOption implements OptionsArg {
    private OptionSpec<String> testers;

    @Override
    public void configure(OptionParser parser) {
        testers = parser.accepts("tester-group", "Comma-separated list of tester groups to be notified on the new build. Or \"all\" to notify all testers").withRequiredArg();
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        if (!optionSet.has(testers))
            return builder;

        String testers = optionSet.valueOf(this.testers);
        if (StringUtils.isEmpty(testers))
            return builder;

        Options.Builder b = builder;
        for (String tester : testers.split(",")) {
            b = (b == null) ? new Options.Builder().addTesterGroup(tester) : b.addTesterGroup(tester);
        }

        return builder;
    }
}
