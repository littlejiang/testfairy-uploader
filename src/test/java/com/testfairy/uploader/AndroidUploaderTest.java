package com.testfairy.uploader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AndroidUploaderTest {
	private static final String API_KEY = "API_KEY";
	private TestFairyService service;
	private TestFairyService.Request request;
	private AndroidAppSigner resigner;

	@Before
	public void before() {
		service = mock(TestFairyService.class);
		request = mock(TestFairyService.Request.class, new SelfReturningAnswer());
		resigner = mock(AndroidAppSigner.class);

		when(service.newRequest()).thenReturn(request);
	}

	@Test
	public void upload_adds_api_key_to_request() {
		AndroidUploader uploader = new AndroidUploader(
			service, resigner, API_KEY, "PATH_TO_APK", null, false, null
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

		uploader.upload(null);

		verify(request, times(2)).addString(keyCaptor.capture(), valueCaptor.capture());
		List<String> allKeys = keyCaptor.getAllValues();
		List<String> allValues = valueCaptor.getAllValues();
		assertEquals(2, allKeys.size());
		assertEquals(2, allValues.size());

		assertEquals("api_key", allKeys.get(0));
		assertEquals(API_KEY, allValues.get(0));
	}

	@Test
	public void upload_adds_options_to_request() {
		Options defaultOptions = new Options.Builder().build();
		AndroidUploader uploader = new AndroidUploader(
			service, resigner, API_KEY, "PATH_TO_APK", null, false, defaultOptions
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

		uploader.upload(null);

		verify(request, times(16)).addString(keyCaptor.capture(), valueCaptor.capture());
		List<String> allKeys = keyCaptor.getAllValues();
		List<String> allValues = valueCaptor.getAllValues();
		assertEquals(16, allKeys.size());
		assertEquals(16, allValues.size());

		int index = 0;
		assertEquals("api_key", allKeys.get(index));
		assertEquals(API_KEY, allValues.get(index++));

		assertEquals("instrumentation", allKeys.get(index));
		assertEquals("off", allValues.get(index++));

		assertEquals("metrics", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("max-duration", allKeys.get(index));
		assertEquals("10m", allValues.get(index++));

		assertEquals("comment", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("video", allKeys.get(index));
		assertEquals("on", allValues.get(index++));

		assertEquals("video-quality", allKeys.get(index));
		assertEquals("high", allValues.get(index++));

		assertEquals("video-rate", allKeys.get(index));
		assertEquals("1.0", allValues.get(index++));

		assertEquals("icon-watermark", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("testers-groups", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("notify", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("auto-update", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("changelog", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("options", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("screenshot-interval", allKeys.get(index));
		assertEquals(null, allValues.get(index++));

		assertEquals("advanced-options", allKeys.get(index));
		assertEquals(null, allValues.get(index++));
	}

	public void upload_appends_file() {
		AndroidUploader uploader = new AndroidUploader(
			service, resigner, API_KEY, "PATH_TO_APK", null, false, null
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<File> valueCaptor = ArgumentCaptor.forClass(File.class);

		uploader.upload(null);

		verify(request).addFile(keyCaptor.capture(), valueCaptor.capture());
		List<String> allKeys = keyCaptor.getAllValues();
		List<File> allValues = valueCaptor.getAllValues();
		assertEquals(1, allKeys.size());
		assertEquals(1, allValues.size());

		assertEquals("api_key", allKeys.get(0));
		assertEquals("PATH_TO_APK", allValues.get(0).getAbsolutePath());
	}

	@Test
	public void upload_notifies_started() {
		AndroidUploader uploader = new AndroidUploader(
			service, resigner, API_KEY, "PATH_TO_APK", null, false, null
		);

		Listener listener = mock(Listener.class);
		uploader.upload(listener);

		verify(listener).onUploadStarted();
	}

	@Test
	public void upload_notifies_completed() {
		AndroidUploader uploader = new AndroidUploader(
			service, resigner, API_KEY, "PATH_TO_APK", null, false, null
		);

		Listener listener = mock(Listener.class);
		uploader.upload(listener);

		verify(listener).onUploadComplete(null);
	}

	@Test
	public void upload_notifies_failures() {
		AndroidUploader uploader = new AndroidUploader(
			service, resigner, API_KEY, "PATH_TO_APK", null, false, null
		);

		Listener listener = mock(Listener.class);
		doThrow(RuntimeException.class)
			.when(request).upload();
		uploader.upload(listener);

		verify(listener).onUploadFailed(any(RuntimeException.class));
	}
}
