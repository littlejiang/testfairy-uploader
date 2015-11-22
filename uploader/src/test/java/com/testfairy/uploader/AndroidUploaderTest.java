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

        uploader.upload( null);

        verify(request, times(16)).addString(keyCaptor.capture(), valueCaptor.capture());
        List<String> allKeys = keyCaptor.getAllValues();
        List<String> allValues = valueCaptor.getAllValues();
        assertEquals(16, allKeys.size());
        assertEquals(16, allValues.size());

        assertEquals("api_key", allKeys.get(0));
        assertEquals(API_KEY, allValues.get(0));

        assertEquals("changelog", allKeys.get(1));
        assertEquals(null, allValues.get(1));

        assertEquals("metrics", allKeys.get(2));
        assertEquals(null, allValues.get(2));

        assertEquals("max-duration", allKeys.get(3));
        assertEquals("10m", allValues.get(3));

        assertEquals("comment", allKeys.get(4));
        assertEquals(null, allValues.get(4));

        assertEquals("video", allKeys.get(5));
        assertEquals("on", allValues.get(5));

        assertEquals("video-quality", allKeys.get(6));
        assertEquals("high", allValues.get(6));

        assertEquals("video-rate", allKeys.get(7));
        assertEquals("1.0", allValues.get(7));

        assertEquals("icon-watermark", allKeys.get(8));
        assertEquals("off", allValues.get(8));

        assertEquals("testers-groups", allKeys.get(9));
        assertEquals(null, allValues.get(9));

        assertEquals("notify", allKeys.get(10));
        assertEquals("on", allValues.get(10));

        assertEquals("auto-update", allKeys.get(11));
        assertEquals("off", allValues.get(11));

        assertEquals("record-on-background", allKeys.get(12));
        assertEquals(null, allValues.get(12));

        assertEquals("screenshot-interval", allKeys.get(13));
        assertEquals(null, allValues.get(13));

        assertEquals("advanced-options", allKeys.get(14));
        assertEquals(null, allValues.get(14));

        assertEquals("data-only-wifi", allKeys.get(15));
        assertEquals(null, allValues.get(15));
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
