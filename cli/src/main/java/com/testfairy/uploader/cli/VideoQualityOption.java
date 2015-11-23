package com.testfairy.uploader.cli;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

class VideoQualityOption implements OptionsArg {

    private OptionSpec<String> videoQuality;

    @Override
    public void configure(OptionParser parser) {
        videoQuality = parser.accepts("video-quality", "Video quality settings, \"high\", \"medium\" or \"low\". Default is \"high\"").withRequiredArg();
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        if (! optionSet.has(videoQuality))
            return builder;

        String videoQuality = optionSet.valueOf(this.videoQuality);
        if ("high".equals(videoQuality)) {
            return builder == null ? new Options.Builder().setVideoQualityHigh() : builder.setVideoQualityHigh();
        } else if ("medium".equals(videoQuality)) {
            return builder == null ? new Options.Builder().setVideoQualityMedium() : builder.setVideoQualityMedium();
        } else if ("low".equals(videoQuality)) {
            return builder == null ? new Options.Builder().setVideoQualityLow() : builder.setVideoQualityLow();
        }

        return builder;
    }
}
