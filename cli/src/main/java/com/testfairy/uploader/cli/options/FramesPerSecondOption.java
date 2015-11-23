package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class FramesPerSecondOption implements OptionsArg {
    private OptionSpec<Float> videoRate;

    @Override
    public void configure(OptionParser parser) {
        videoRate = parser.accepts("video-rate", "Video rate recording in frames per second").withRequiredArg().ofType(Float.class);
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        if (!optionSet.has(videoRate))
            return builder;

        Float videoRate = optionSet.valueOf(this.videoRate);
        if (videoRate == null)
            return builder;

        return builder == null ? new Options.Builder().setVideoRecordingRate(videoRate) : builder.setVideoRecordingRate(videoRate);
    }
}
