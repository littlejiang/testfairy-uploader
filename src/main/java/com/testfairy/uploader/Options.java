package com.testfairy.uploader;

import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;

public class Options {
	public static class Builder {
		private ArrayList<String> testers;
		private Boolean notify;
		private Boolean anonymous;
		private Boolean autoUpdate;
		private Boolean watermarkIcon;
		private Boolean shake;
		private Boolean recordOnBackground;
		private Boolean dataOnlyWifi;
		private String maxDuration;
		private String comment;
		private String videoRecording;
		private String videoQuality;
		private String changelog;
		private String advancedOptions;
		private float framesPerSecond;
		private Float screenshotInterval;

		public Builder() {
			this.maxDuration = "10m";
			this.videoRecording = "on";
			this.framesPerSecond = 1.0f;
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

		public Builder setScreenshotInterval(float screenshotInterval) {
			this.screenshotInterval = screenshotInterval;
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

		public Builder setChangelog(String changelog) {
			this.changelog = changelog;
			return this;
		}

		public Builder setAdvancedOptions(String advancedOptions) {
			this.advancedOptions = advancedOptions;
			return this;
		}

		public Builder shakeForBugReports(boolean shake) {
			this.shake = shake;
			return this;
		}

		public Builder setDataOnlyWifi() {
			this.dataOnlyWifi = true;
			return this;
		}

		public Builder setRecordInBackground() {
			this.recordOnBackground = true;
			return this;
		}

		public Options build() {
			// TODO: Validate maxDurations
			assertNotEmpty("maxDuration", this.maxDuration);
			String testers = this.testers == null ? null : Strings.join(this.testers, ",");
			if (framesPerSecond < 0) throw new IllegalArgumentException("Frame rate cannot less than 0.0");
			if (screenshotInterval != null && screenshotInterval <= 0) throw new IllegalArgumentException("Screen capture interval cannot be less than 0.0");

			comment = checkIfFile(comment);

			return new Options(
				notify,
				anonymous,
				autoUpdate,
				watermarkIcon,
				shake,
				recordOnBackground,
				dataOnlyWifi,
				testers,
				maxDuration,
				comment,
				videoRecording,
				videoQuality,
				changelog,
				advancedOptions,
				String.valueOf(framesPerSecond),
				screenshotInterval == null ? null : String.valueOf(screenshotInterval)
			);
		}

		private String checkIfFile(String comment) {
			if (Strings.isEmpty(comment)) {
				return comment;
			}

			if (! comment.startsWith("@")) {
				return comment;
			}

			try {
				String path = comment.substring(1);
				File commitMessage = new File(path);

				return IOUtils.toString(new FileInputStream(commitMessage));
			} catch (Exception exception) {
				throw new RuntimeException(exception);
			}
		}

		private static void assertNotEmpty(String key, String value) {
			if (Strings.isEmpty(value)) throw new IllegalArgumentException(key + " cannot be empty");
		}
	}

	final Boolean notify;
	final Boolean anonymous;
	final Boolean autoUpdate;
	final Boolean watermarkIcon;
	final Boolean shake;
	final Boolean recordOnBackground;
	final Boolean dataOnlyWifi;
	final String testers;
	final String maxDuration;
	final String comment;
	final String videoRecording;
	final String videoQuality;
	final String changelog;
	final String advancedOptions;
	final String framesPerSecond;
	final String screenInterval;

	Options(
		Boolean notify,
		Boolean anonymous,
		Boolean autoUpdate,
		Boolean watermarkIcon,
		Boolean shake,
		Boolean recordOnBackground,
		Boolean dataOnlyWifi,
		String testers,
		String duration,
		String comment,
		String videoRecording,
		String videoQuality,
	    String changelog,
		String advancedOptions,
		String framesPerSecond,
	    String screenInterval
	) {
		this.recordOnBackground = recordOnBackground;
		this.dataOnlyWifi = dataOnlyWifi;
		this.testers = testers;
		this.notify = notify;
		this.anonymous = anonymous;
		this.autoUpdate = autoUpdate;
		this.watermarkIcon = watermarkIcon;
		this.shake = shake;
		this.maxDuration = duration;
		this.comment = comment;
		this.videoRecording = videoRecording;
		this.videoQuality = videoQuality;
		this.framesPerSecond = framesPerSecond;
		this.changelog = changelog;
		this.advancedOptions  = advancedOptions;
		this.screenInterval = screenInterval;
	}

	static String optional(Options options) {
		if (options == null)
			return null;

		List<String> value = new ArrayList<>();
		if (isTrue(options.anonymous))
			value.add("anonymous");

		if (isTrue(options.shake))
			value.add("shake");

		if (isTrue(options.recordOnBackground))
			value.add("record-on-background");

		if (isTrue(options.dataOnlyWifi))
			value.add("data-only-wifi");

		return value.isEmpty() ? null : Strings.join(value, ",");
	}

	static void validateForAndroid(Options options, boolean enableInstrumentation) {
		if (options == null) return;

		if (!enableInstrumentation && isTrue(options.watermarkIcon))
			throw new IllegalStateException("Watermarking app icon is only supported when instrumentation is enabled");
	}

	static void validateForIOS(Options options) {
		if (options == null) return;

		if (isTrue(options.anonymous))
			throw new IllegalStateException("Anonymous is support is not available with iOS builds");

		if (isTrue(options.watermarkIcon))
			throw new IllegalStateException("Watermarking app icon support is not available with iOS builds");
	}

	private static boolean isTrue(Boolean value) {
		return value != null && value;
	}

	@Override
	public String toString() {
		return "Options{" +
			"notify=" + notify +
			", anonymous=" + anonymous +
			", autoUpdate=" + autoUpdate +
			", watermarkIcon=" + watermarkIcon +
			", shake=" + shake +
			", recordOnBackground=" + recordOnBackground +
			", dataOnlyWifi=" + dataOnlyWifi +
			", testers='" + testers + '\'' +
			", maxDuration='" + maxDuration + '\'' +
			", comment='" + comment + '\'' +
			", videoRecording='" + videoRecording + '\'' +
			", videoQuality='" + videoQuality + '\'' +
			", changelog='" + changelog + '\'' +
			", advancedOptions='" + advancedOptions + '\'' +
			", framesPerSecond='" + framesPerSecond + '\'' +
			", screenInterval='" + screenInterval + '\'' +
			'}';
	}
}
