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
public class IosUploaderTest {
	private static final String API_KEY = "API_KEY";
	private TestFairyService service;
	private TestFairyService.Request request;

	@Before
	public void before() {
		service = mock(TestFairyService.class);
		request = mock(TestFairyService.Request.class, new SelfReturningAnswer());

		when(service.newRequest()).thenReturn(request);
	}

	@Test
	public void upload_adds_api_key_to_request() {
		IosUploader uploader = new IosUploader(
			service, API_KEY, "PATH_TO_IPA", null, null
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

		uploader.upload(null);

		verify(request).addString(keyCaptor.capture(), valueCaptor.capture());
		List<String> allKeys = keyCaptor.getAllValues();
		List<String> allValues = valueCaptor.getAllValues();
		assertEquals(1, allKeys.size());
		assertEquals(1, allValues.size());

		assertEquals("api_key", allKeys.get(0));
		assertEquals(API_KEY, allValues.get(0));
	}

	@Test
	public void upload_adds_options_to_request() {
		Options defaultOptions = new Options.Builder().build();
		IosUploader uploader = new IosUploader(
			service, API_KEY, "PATH_TO_IPA", null, defaultOptions
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

		uploader.upload(null);

		verify(request, times(15)).addString(keyCaptor.capture(), valueCaptor.capture());
		List<String> allKeys = keyCaptor.getAllValues();
		List<String> allValues = valueCaptor.getAllValues();
		assertEquals(15, allKeys.size());
		assertEquals(15, allValues.size());

		assertEquals("api_key", allKeys.get(0));
		assertEquals(API_KEY, allValues.get(0));

		assertEquals("metrics", allKeys.get(1));
		assertEquals(null, allValues.get(1));

		assertEquals("max-duration", allKeys.get(2));
		assertEquals("10m", allValues.get(2));

		assertEquals("comment", allKeys.get(3));
		assertEquals(null, allValues.get(3));

		assertEquals("video", allKeys.get(4));
		assertEquals("on", allValues.get(4));

		assertEquals("video-quality", allKeys.get(5));
		assertEquals("high", allValues.get(5));

		assertEquals("video-rate", allKeys.get(6));
		assertEquals("1.0", allValues.get(6));

		assertEquals("icon-watermark", allKeys.get(7));
		assertEquals("off", allValues.get(7));

		assertEquals("testers-groups", allKeys.get(8));
		assertEquals(null, allValues.get(8));

		assertEquals("notify", allKeys.get(9));
		assertEquals("on", allValues.get(9));

		assertEquals("auto-update", allKeys.get(10));
		assertEquals("off", allValues.get(10));

		assertEquals("record-on-background", allKeys.get(11));
		assertEquals(null, allValues.get(11));

		assertEquals("screenshot-interval", allKeys.get(12));
		assertEquals(null, allValues.get(12));

		assertEquals("advanced-options", allKeys.get(13));
		assertEquals(null, allValues.get(13));

		assertEquals("data-only-wifi", allKeys.get(14));
		assertEquals(null, allValues.get(14));
	}

	public void upload_appends_file() {
		IosUploader uploader = new IosUploader(
			service, API_KEY, "PATH_TO_IPA", null, null
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
		assertEquals("PATH_TO_IPA", allValues.get(0).getAbsolutePath());
	}

	@Test
	public void upload_notifies_started() {
		IosUploader uploader = new IosUploader(
			service, API_KEY, "PATH_TO_IPA", null, null
		);

		Listener listener = mock(Listener.class);
		uploader.upload(listener);

		verify(listener).onUploadStarted();
	}

	@Test
	public void upload_notifies_completed() {
		IosUploader uploader = new IosUploader(
			service, API_KEY, "PATH_TO_IPA", null, null
		);

		Listener listener = mock(Listener.class);
		uploader.upload(listener);

		verify(listener).onUploadComplete();
	}

	@Test
	public void upload_notifies_failures() {
		IosUploader uploader = new IosUploader(
			service, API_KEY, "PATH_TO_IPA", null, null
		);

		Listener listener = mock(Listener.class);
		doThrow(RuntimeException.class)
			.when(request).upload();
		uploader.upload(listener);

		verify(listener).onUploadFailed(any(RuntimeException.class));
	}
}
