package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class WatermarkOption implements OptionsArg {
    private OptionSpec<Void> watermark;

    @Override
    public void configure(OptionParser parser) {
        watermark = parser.accepts("watermark", "Add a small watermark to app icon");
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        if (!optionSet.has(watermark))
            return builder;

        return builder == null ? new Options.Builder().setIconWatermark(true) : builder.setIconWatermark(true);
    }
}
