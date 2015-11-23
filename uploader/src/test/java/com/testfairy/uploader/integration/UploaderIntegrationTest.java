package com.testfairy.uploader.integration;

import com.testfairy.uploader.*;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import java.net.URL;

@RunWith(MockitoJUnitRunner.class)
public class UploaderIntegrationTest {
	private static final String API_KEY = "7ac85171ea92ad548396846dc4310e1e625fe65a";
	private static final String IPA_PATH = UploaderIntegrationTest.class.getClassLoader().getResource("test-ipa.ipa").getPath();
	private static final String APK_PATH = UploaderIntegrationTest.class.getClassLoader().getResource("test-apk.apk").getPath();
	private static final String KEYSTORE_PATH = UploaderIntegrationTest.class.getClassLoader().getResource("debug.keystore").getPath();
	private static final String KEYSTORE_ALIAS = "androiddebugkey";
	private static final String KEYSTORE_PASSWORD = "android";

	@Test
	public void upload_iOS_IPA() {
		Options options = new Options.Builder()
			.notifyTesters(true)
			.setComment("Uploading iOS IPA")
			.build();
		IOSUploader uploader = new IOSUploader.Builder(API_KEY)
			.setOptions(options)
			.setIpaPath(IPA_PATH)
			.build();

		uploader.upload(new ListenerAdapter() {
			@Override
			public void onUploadComplete(Build build) {
				Assert.assertEquals("ok", build.status());
			}
		});
	}

	@Test
	public void upload_iOS_APK_unsigned() {
		Options options = new Options.Builder()
			.notifyTesters(true)
			.setComment("Uploading iOS IPA")
			.build();
		AndroidUploader uploader = new AndroidUploader.Builder(API_KEY)
			.setOptions(options)
			.enableInstrumentation(false)
			.setApkPath(APK_PATH)
			.build();

		uploader.upload(new ListenerAdapter() {
			@Override
			public void onUploadComplete(Build build) {
				Assert.assertEquals("ok", build.status());
			}
		});
	}

	@Test
	public void upload_iOS_APK_signed() {
		Options options = new Options.Builder()
			.notifyTesters(true)
			.setComment("Uploading iOS IPA")
			.build();
		AndroidUploader uploader = new AndroidUploader.Builder(API_KEY)
			.setOptions(options)
			.setApkPath(APK_PATH)
			.setKeystore(KEYSTORE_PATH, KEYSTORE_ALIAS, KEYSTORE_PASSWORD, null)
			.build();

		uploader.upload(new ListenerAdapter() {
			@Override
			public void onUploadComplete(Build build) {
				Assert.assertEquals("ok", build.status());
			}
		});
	}

	private static class ListenerAdapter implements Listener {
		@Override public void onUploadStarted() {}
		@Override public void onUploadComplete(Build build) {}
		@Override public void onUploadFailed(Throwable throwable) {
			throw new RuntimeException(throwable);
		}
		@Override public void onProgress(float p) {}
	}
}
