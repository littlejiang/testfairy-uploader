package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Options;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class VideoRecordingOption implements OptionsArg {
    private OptionSpec<String> videoRecording;

    @Override
    public void configure(OptionParser parser) {
        videoRecording = parser.accepts("video", "Video recording settings \"on\", \"off\" or \"wifi\" for recording video only when wifi is available. Default is \"on\"").withRequiredArg();
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        if (! optionSet.has(videoRecording))
            return builder;

        String videoQuality = optionSet.valueOf(this.videoRecording);
        if ("on".equals(videoQuality)) {
            return builder == null ? new Options.Builder().setVideoRecordingOn() : builder.setVideoRecordingOn();
        } else if ("off".equals(videoQuality)) {
            return builder == null ? new Options.Builder().setVideoRecordingOff() : builder.setVideoRecordingOff();
        } else if ("wifi".equals(videoQuality)) {
            return builder == null ? new Options.Builder().setVideoRecordingWifi() : builder.setVideoRecordingWifi();
        }

        return builder;
    }
}
