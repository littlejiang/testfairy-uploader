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
		assertEquals(null, options.notify);
		assertEquals(null, options.anonymous);
		assertEquals(null, options.autoUpdate);
		assertEquals(null, options.watermarkIcon);
		assertEquals(null, options.testers);
		assertEquals(null, options.metrics);
		assertEquals(null, options.comment);
		assertEquals("10m", options.maxDuration);
		assertEquals("on", options.videoRecording);
		assertEquals("high", options.videoQuality);
		assertEquals("1.0", options.framesPerSecond);
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
}
