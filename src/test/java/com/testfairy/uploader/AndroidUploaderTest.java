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

	@Before
	public void before() {
		service = mock(TestFairyService.class);
		request = mock(TestFairyService.Request.class, new SelfReturningAnswer());

		when(service.newRequest()).thenReturn(request);
	}

	@Test
	public void upload_adds_api_key_to_request() {
		AndroidUploader uploader = new AndroidUploader(
			service, API_KEY, null
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

		uploader.upload("PATH_TO_IPA", null);

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
		AndroidUploader uploader = new AndroidUploader(
			service, API_KEY, defaultOptions
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<String> valueCaptor = ArgumentCaptor.forClass(String.class);

		uploader.upload("PATH_TO_IPA", null);

		verify(request, times(8)).addString(keyCaptor.capture(), valueCaptor.capture());
		List<String> allKeys = keyCaptor.getAllValues();
		List<String> allValues = valueCaptor.getAllValues();
		assertEquals(8, allKeys.size());
		assertEquals(8, allValues.size());

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
	}

	public void upload_appends_file() {
		AndroidUploader uploader = new AndroidUploader(
			service, API_KEY, null
		);

		ArgumentCaptor<String> keyCaptor = ArgumentCaptor.forClass(String.class);
		ArgumentCaptor<File> valueCaptor = ArgumentCaptor.forClass(File.class);

		uploader.upload("PATH_TO_IPA", null);

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
		AndroidUploader uploader = new AndroidUploader(
			service, API_KEY, null
		);

		Listener listener = mock(Listener.class);
		uploader.upload("PATH_TO_IPA", listener);

		verify(listener).onUploadStarted();
	}

	@Test
	public void upload_notifies_completed() {
		AndroidUploader uploader = new AndroidUploader(
			service, API_KEY, null
		);

		Listener listener = mock(Listener.class);
		uploader.upload("PATH_TO_IPA", listener);

		verify(listener).onUploadComplete();
	}

	@Test
	public void upload_notifies_failures() {
		AndroidUploader uploader = new AndroidUploader(
			service, API_KEY, null
		);

		Listener listener = mock(Listener.class);
		doThrow(RuntimeException.class)
			.when(request).upload();
		uploader.upload("PATH_TO_IPA", listener);

		verify(listener).onUploadFailed(any(RuntimeException.class));
	}
}
