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
		private Boolean videoOnlyWifi;
		private Metrics metrics;
		private String maxDuration;
		private String comment;
		private String videoRecording;
		private String videoQuality;
		private String changelog;
		private String advancedOptions;
		private float framesPerSecond;

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

//		public Builder setVideoOnlyWifi(boolean videoOnlyWifi) {
//			this.videoOnlyWifi = videoOnlyWifi;
//			return this;
//		}

		public Builder setRecordInBackground() {
			this.recordOnBackground = true;
			return this;
		}

		public Options build() {
			// TODO: Validate maxDurations
			assertNotEmpty("maxDuration", this.maxDuration);
			String testers = this.testers == null ? null : Strings.join(this.testers, ",");
			String metrics = this.metrics == null ? null : this.metrics.asFormString();
			if (framesPerSecond < 1.0f) throw new IllegalArgumentException("Frame rate cannot less than 1.0");
			comment = checkIfFile(comment);

			return new Options(
				notify,
				anonymous,
				autoUpdate,
				watermarkIcon,
				shake,
				recordOnBackground,
				dataOnlyWifi,
				videoOnlyWifi,
				testers,
				metrics,
				maxDuration,
				comment,
				videoRecording,
				videoQuality,
				String.valueOf(framesPerSecond),
				changelog,
				advancedOptions
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
	final Boolean videoOnlyWifi;
	final String testers;
	final String metrics;
	final String maxDuration;
	final String comment;
	final String videoRecording;
	final String videoQuality;
	final String framesPerSecond;
	final String changelog;
	final String advancedOptions;

	Options(
		Boolean notify,
		Boolean anonymous,
		Boolean autoUpdate,
		Boolean watermarkIcon,
		Boolean shake,
		Boolean recordOnBackground,
		Boolean dataOnlyWifi,
		Boolean videoOnlyWifi,
		String testers,
		String metrics,
		String duration,
		String comment,
		String videoRecording,
		String videoQuality,
		String framesPerSecond,
	    String changelog,
	    String advancedOptions
	) {
		this.recordOnBackground = recordOnBackground;
		this.dataOnlyWifi = dataOnlyWifi;
		this.videoOnlyWifi = videoOnlyWifi;
		this.testers = testers;
		this.notify = notify;
		this.anonymous = anonymous;
		this.autoUpdate = autoUpdate;
		this.watermarkIcon = watermarkIcon;
		this.shake = shake;
		this.metrics = metrics;
		this.maxDuration = duration;
		this.comment = comment;
		this.videoRecording = videoRecording;
		this.videoQuality = videoQuality;
		this.framesPerSecond = framesPerSecond;
		this.changelog = changelog;
		this.advancedOptions  = advancedOptions;
	}

	static String optional(Options options) {
		if (options == null)
			return null;

		List<String> value = new ArrayList<>();
		if (options.anonymous != null && options.anonymous)
			value.add("anonymous");

		if (options.shake != null && options.shake)
			value.add("shake");

		if (options.recordOnBackground != null && options.recordOnBackground)
			value.add("record-on-background");

		if (options.dataOnlyWifi!= null && options.dataOnlyWifi)
			value.add("data-only-wifi");

		if (options.videoOnlyWifi != null && options.videoOnlyWifi)
			value.add("video-only-wifi");

		return value.isEmpty() ? null : Strings.join(value, ",");
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
			", videoOnlyWifi=" + videoOnlyWifi +
			", testers='" + testers + '\'' +
			", metrics='" + metrics + '\'' +
			", maxDuration='" + maxDuration + '\'' +
			", comment='" + comment + '\'' +
			", videoRecording='" + videoRecording + '\'' +
			", videoQuality='" + videoQuality + '\'' +
			", framesPerSecond='" + framesPerSecond + '\'' +
			", changelog='" + changelog + '\'' +
			'}';
	}
}
