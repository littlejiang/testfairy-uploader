package com.testfairy.uploader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class OptionsTest {

	@Test
	public void new_options_has_default_values() {
		Options options = new Options.Builder().build();
		assertEquals(null, options.testers);
		assertEquals(null, options.notify);
		assertEquals(null, options.anonymous);
		assertEquals(null, options.autoUpdate);
		assertEquals(null, options.watermarkIcon);
		assertEquals(null, options.shake);
		assertEquals(null, options.recordOnBackground);
		assertEquals(null, options.dataOnlyWifi);
		assertEquals(null, options.videoOnlyWifi);
		assertEquals(null, options.metrics);
		assertEquals("10m", options.maxDuration);
		assertEquals(null, options.comment);
		assertEquals("on", options.videoRecording);
		assertEquals("high", options.videoQuality);
		assertEquals(null, options.changelog);
		assertEquals(null, options.advancedOptions);
		assertEquals("1.0", options.framesPerSecond);
		assertEquals(null, options.screenInterval);
	}

	@Test
	public void notifyTesters_sets_expected_value() {
		Options options = new Options.Builder()
			.notifyTesters(false)
			.build();

		assertEquals(false, options.notify);
	}

	@Test
	public void setAnonymous_sets_expected_value() {
		Options options = new Options.Builder()
			.setAnonymous(true)
			.build();

		assertEquals(true, options.anonymous);
	}

	@Test
	public void setAutoUpdate_sets_expected_value() {
		Options options = new Options.Builder()
			.setAutoUpdate(true)
			.build();

		assertEquals(true, options.autoUpdate);
	}

	@Test
	public void setIconWatermark_sets_expected_value() {
		Options options = new Options.Builder()
			.setIconWatermark(true)
			.build();

		assertEquals(true, options.watermarkIcon);
	}

	@Test
	public void setComment_sets_expected_value() {
		String expectedComment = "This comment";
		Options options = new Options.Builder()
			.setComment(expectedComment)
			.build();

		assertEquals(expectedComment, options.comment);
	}

	@Test
	public void addTesterGroup_sets_expected_value() {
		String group = "Alpha";
		Options options = new Options.Builder()
			.addTesterGroup(group)
			.build();

		assertEquals(group, options.testers);
	}

	@Test
	public void multiple_addTesterGroup_sets_expected_value() {
		String group1 = "Alpha";
		String group2 = "Beta";
		Options options = new Options.Builder()
			.addTesterGroup(group1)
			.addTesterGroup(group2)
			.build();

		assertEquals("Alpha,Beta", options.testers);
	}

	@Test
	public void setMaxDuration_sets_expected_value() {
		String expectedDuration = "24h";
		Options options = new Options.Builder()
				.setMaxDuration(expectedDuration)
				.build();

		assertEquals(expectedDuration, options.maxDuration);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setMaxDuration_cannot_be_set_to_null() {
		new Options.Builder()
			.setMaxDuration(null)
			.build();
	}

	@Test
	public void setVideoRecordingRate_sets_expected_value() {
		float expectedRate = 2.0f;
		Options options = new Options.Builder()
			.setVideoRecordingRate(expectedRate)
			.build();

		assertEquals("2.0", options.framesPerSecond);
	}

	@Test(expected = IllegalArgumentException.class)
	public void setVideoRecordingRate_cannot_be_set_to_null() {
		new Options.Builder()
			.setVideoRecordingRate(-1.0f)
			.build();
	}

	@Test
	public void setVideoQualityHigh_sets_expected_value() {
		Options options = new Options.Builder()
			.setVideoQualityHigh()
			.build();

		assertEquals("high", options.videoQuality);
	}

	@Test
	public void setVideoQualityMedium_sets_expected_value() {
		Options options = new Options.Builder()
			.setVideoQualityMedium()
			.build();

		assertEquals("medium", options.videoQuality);
	}

	@Test
	public void setVideoQualityLow_sets_expected_value() {
		Options options = new Options.Builder()
			.setVideoQualityLow()
			.build();

		assertEquals("low", options.videoQuality);
	}

	@Test
	public void setVideoRecordingOn_sets_expected_value() {
		Options options = new Options.Builder()
			.setVideoRecordingOn()
			.build();

		assertEquals("on", options.videoRecording);
	}

	@Test
	public void setVideoRecordingOff_sets_expected_value() {
		Options options = new Options.Builder()
			.setVideoRecordingOff()
			.build();

		assertEquals("off", options.videoRecording);
	}

	@Test
	public void setVideoRecordingWifi_sets_expected_value() {
		Options options = new Options.Builder()
			.setVideoRecordingWifi()
			.build();

		assertEquals("wifi", options.videoRecording);
	}

	@Test
	public void setMetrics_sets_expected_value() {
		Metrics metrics = Mockito.mock(Metrics.class);
		String expectedMetrics = "wifi,cpu";
		Mockito.when(metrics.asFormString()).thenReturn(expectedMetrics);

		Options options = new Options.Builder()
			.setMetrics(metrics)
			.build();

		assertEquals(expectedMetrics, options.metrics);
	}

	@Test
	public void setShake_sets_expected_value() {
		Options options = new Options.Builder()
			.shakeForBugReports(true)
			.build();

		assertEquals(true, options.shake);
	}

	@Test
	public void setChangelog_sets_expected_value() {
		String expectedChange = "changes";
		Options options = new Options.Builder()
			.setChangelog(expectedChange)
			.build();

		assertEquals(expectedChange, options.changelog);
	}

	@Test
	public void setAdvancedOptions_sets_expected_value() {
		String expectedChange = "changes";
		Options options = new Options.Builder()
			.setAdvancedOptions(expectedChange)
			.build();

		assertEquals(expectedChange, options.advancedOptions);
	}

	@Test
	public void setScreenshotInterval_sets_expected_value() {
		float value = 4.0f;
		Options options = new Options.Builder()
			.setScreenshotInterval(value)
			.build();

		assertEquals(String.valueOf(value), options.screenInterval);
	}

	@Test
	public void optionals_returns_null_when_null() {
		Options options = null;
		assertEquals(null, Options.optional(options));
	}

	@Test
	public void optionals_returns_null_when_empty() {
		Options options = new Options.Builder().build();
		assertEquals(null, Options.optional(options));
	}

	@Test
	public void optionals_adds_anonymous_when_true() {
		Options options = new Options.Builder()
			.setAnonymous(true)
			.build();
		assertEquals("anonymous", Options.optional(options));
	}

	@Test
	public void optionals_doesnt_add_anonymous_when_false() {
		Options options = new Options.Builder()
			.setAnonymous(false)
			.build();
		assertEquals(null, Options.optional(options));
	}

	@Test
	public void optionals_adds_shake_when_true() {
		Options options = new Options.Builder()
			.shakeForBugReports(true)
			.build();
		assertEquals("shake", Options.optional(options));
	}

	@Test
	public void optionals_doesnt_add_shake_when_false() {
		Options options = new Options.Builder()
			.shakeForBugReports(false)
			.build();
		assertEquals(null, Options.optional(options));
	}

	@Test
	public void optionals_adds_recordOnBackground_when_set() {
		Options options = new Options.Builder()
			.setRecordInBackground()
			.build();
		assertEquals("record-on-background", Options.optional(options));
	}

	@Test
	public void optionals_adds_dataOnlyWifi_when_set() {
		Options options = new Options.Builder()
			.setDataOnlyWifi()
			.build();
		assertEquals("data-only-wifi", Options.optional(options));
	}

	@Test
	public void options_adds_options_in_expected_order() {
		Options options = new Options.Builder()
			.shakeForBugReports(true)
			.setAnonymous(true)
			.setRecordInBackground()
			.setDataOnlyWifi()
			.build();

		assertEquals("anonymous,shake,record-on-background,data-only-wifi", Options.optional(options));
	}

	@Test
	public void validateForIOS_ignores_null_options() {
		Options.validateForIOS(null);
	}

	@Test(expected = IllegalStateException.class)
	public void validateForIOS_throws_when_anonymous_is_enabled() {
		Options.validateForIOS(new Options.Builder().setAnonymous(true).build());
	}

	@Test
	public void validateForIOS_ignores_when_anonymous_is_disabled() {
		Options.validateForIOS(new Options.Builder().setAnonymous(false).build());
	}

	@Test(expected = IllegalStateException.class)
	public void validateForIOS_throws_when_watermarking_is_enabled() {
		Options.validateForIOS(new Options.Builder().setIconWatermark(true).build());
	}

	@Test
	public void validateForIOS_ignores_when_watermarking_is_disabled() {
		Options.validateForIOS(new Options.Builder().setIconWatermark(false).build());
	}

	@Test
	public void validateForAndroid_without_instrumentation_ignores_null_options() {
		Options.validateForAndroid(null, false);
	}

	@Test
	public void validateForAndroid_with_instrumentation_ignores_null_options() {
		Options.validateForAndroid(null, true);
	}

	@Test(expected = IllegalStateException.class)
	public void validateForAndroid_without_instrumentation_throws_when_watermarking_is_enabled() {
		Options.validateForAndroid(new Options.Builder().setIconWatermark(true).build(), false);
	}

	@Test
	public void validateForAndroid_with_instrumentation_ignores_when_watermarking_is_enabled() {
		Options.validateForAndroid(new Options.Builder().setIconWatermark(true).build(), true);
	}

	@Test
	public void validateForAndroid_without_instrumentation_ignores_when_watermarking_is_disabled() {
		Options.validateForAndroid(new Options.Builder().setIconWatermark(false).build(), false);
	}

	@Test
	public void validateForAndroid_with_instrumentation_ignores_when_watermarking_is_disabled() {
		Options.validateForAndroid(new Options.Builder().setIconWatermark(false).build(), true);
	}
}
