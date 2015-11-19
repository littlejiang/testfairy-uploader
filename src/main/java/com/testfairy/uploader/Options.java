package com.testfairy.uploader;

/*
symbols_file Symbols mapping file.
testers_groups Comma-separated list of tester groups to be notified on the new build. Or "all" to notify all testers.
metrics Comma-separated list of metrics to record. View list below.
max-duration Maximum session recording length, eg 20m or 1h. Default is "10m". Maximum 24h.
video Video recording settings "on", "off" or "wifi" for recording video only when wifi is available. Default is "on".
video-quality Video quality settings, "high", "medium" or "low". Default is "high".
video-rate Video rate recording in frames per second, default is "1.0".
icon-watermark Add a small watermark to app icon. Default is "off".
comment Additional release notes for this upload. This text will be added to email notifications.
auto-update Allows easy upgrade of all users to current version. Set to "on" to enable. Default is "off".
notify "on" or "off"
*/

import java.util.ArrayList;

/*
shake - Use this option to let the tester to shake their device and fill in a bug report that openes up.
video-only-wifi - This option can be used in cases where you wish not to use the testers bandwidth.
anonymous - When using this option, sessions are anonymous and account information is not collected from device.
*/
public class Options {
    public static class Builder {
        private ArrayList<String> testers;
        private boolean notify;
        private boolean anonymous;
        private boolean autoUpdate;
        private boolean watermarkIcon;
        private Metrics metrics;
        private String maxDuration;
        private String comment;
        private String videoRecording;
        private String videoQuality;
        private float framesPerSecond;

        public Builder() {
            this.notify = true;
            this.anonymous = false;
            this.maxDuration = "10m";
            this.videoRecording = "on";
            this.framesPerSecond = 1.0f;
            this.autoUpdate = false;
            this.watermarkIcon = false;
            this.videoQuality = "high";
        }

        public Builder addTesterGroup(String group) {
            if (this.testers == null)
                this.testers  = new ArrayList<String>();

            this.testers.add(group);
            return this;
        }

        public Builder notifyTesters(boolean notify) {
            this.notify = notify;
            return this;
        }

        public Builder setMetrics(Metrics metrics) {
            this.metrics = metrics;
            return this;
        }

        public Builder setAnonymous(boolean anonymous) {
            this.anonymous = anonymous;
            return this;
        }

        public Builder setMaxDuration(String duration) {
            this.maxDuration = duration;
            return this;
        }

        public Builder setVideoRecordingOn() {
            this.videoRecording = "on";
            return this;
        }

        public Builder setVideoRecordingOff() {
            this.videoRecording = "off";
            return this;
        }

        public Builder setVideoRecordingWifi() {
            this.videoRecording = "wifi";
            return this;
        }

        public Builder setVideoRecordingRate(float fps) {
            this.framesPerSecond = fps;
            return this;
        }

        public Builder setVideoQualityHigh() {
            this.videoQuality = "high";
            return this;
        }

        public Builder setVideoQualityMedium() {
            this.videoQuality = "medium";
            return this;
        }

        public Builder setVideoQualityLow() {
            this.videoQuality = "low";
            return this;
        }

        public Builder setIconWatermark(boolean watermark) {
            this.watermarkIcon = watermark;
            return this;
        }

        public Builder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Builder setAutoUpdate(boolean autoUpdate) {
            this.autoUpdate = autoUpdate;
            return this;
        }

        public Options build() {
            // TODO: Validate maxDurations
            assertNotEmpty("maxDuration", this.maxDuration);
            String testers = this.testers == null ? null : Strings.join(this.testers, ",");
            String metrics = this.metrics == null ? null : this.metrics.asFormString();
            if (framesPerSecond < 1.0f) throw new IllegalArgumentException("Frame rate cannot less than 1.0");

            return new Options(
                notify,
                anonymous,
                autoUpdate,
                watermarkIcon,
                testers,
                metrics,
                maxDuration,
                comment,
                videoRecording,
                videoQuality,
                String.valueOf(framesPerSecond)
            );
        }

        private static void assertNotEmpty(String key, String value) {
            if (Strings.isEmpty(value)) throw new IllegalArgumentException(key + " cannot be empty");
        }
    }

    final boolean notify;
    final boolean anonymous;
    final boolean autoUpdate;
    final boolean watermarkIcon;
    final String testers;
    final String metrics;
    final String maxDuration;
    final String comment;
    final String videoRecording;
    final String videoQuality;
    final String framesPerSecond;

    Options(
        boolean notify,
        boolean anonymous,
        boolean autoUpdate,
        boolean watermarkIcon,
        String testers,
        String metrics,
        String duration,
        String comment,
        String videoRecording,
        String videoQuality,
        String framesPerSecond
    ) {
        this.testers = testers;
        this.notify = notify;
        this.anonymous = anonymous;
        this.autoUpdate = autoUpdate;
        this.watermarkIcon = watermarkIcon;
        this.metrics = metrics;
        this.maxDuration = duration;
        this.comment = comment;
        this.videoRecording = videoRecording;
        this.videoQuality = videoQuality;
        this.framesPerSecond = framesPerSecond;
    }
}
