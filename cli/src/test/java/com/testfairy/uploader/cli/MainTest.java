package com.testfairy.uploader.cli;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class MainTest {
	private static final String API_KEY = "7ac85171ea92ad548396846dc4310e1e625fe65a";
	private static final String IPA_PATH = MainTest.class.getClassLoader().getResource("test-ipa.ipa").getPath();
	private static final String APK_PATH = MainTest.class.getClassLoader().getResource("test-apk.apk").getPath();
	private static final String INVALID_FILE = MainTest.class.getClassLoader().getResource("invalid-file.txt").getPath();

	@Test
	public void returns_error_when_file_does_not_exist() {
		Assert.assertEquals(-1, run("/Users/downloads/SampleApp.apk"));
	}

	@Test
	public void returns_when_no_file_is_given() {
		Assert.assertEquals(-1, run(""));
	}

	@Test
	public void returns_when_no_file_is_given_but_contains_arguments() {
		Assert.assertEquals(-3, run("--api-key=" + API_KEY));
	}

	@Test
	public void returns_error_when_file_does_not_exist_but_contains_arguments() {
		Assert.assertEquals(-4, run("--api-key=" + API_KEY + " /Users/downloads/SampleApp.apk"));
	}

	@Test
	public void returns_error_when_file_is_invalid() {
		Assert.assertEquals(-5, run("--api-key=" + API_KEY + " " + INVALID_FILE));
	}

	@Test
	public void returns_error_when_api_key_is_missing() {
		Assert.assertEquals(-1, run(IPA_PATH));
	}

	@Test
	public void returns_error_when_api_key_is_empty() {
		Assert.assertEquals(-3, run("--api-key " + IPA_PATH));
	}

	@Test
	public void returns_success_when_viewing_usage() {
		Assert.assertEquals(0, run("--help"));
	}

	@Test
	public void can_upload_ios_builds() {
		Assert.assertEquals(0, run("--api-key=" + API_KEY + " " + IPA_PATH));
	}

	private int run(String input) {
		return new Main().execute(input.split(" "));
	}
}
