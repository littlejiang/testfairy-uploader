package com.testfairy.uploader;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ZipAlignerTest {
	private static final String PATH_TO_ZIPALIGNER = "/path/to/zipaligner";
	private static final String PATH_TO_OUTPUT = "/path/to/output";
	private static final String PATH_TO_APK = "/path/to/apk";

	private ZipAligner aligner;
	@Mock private ProcessExecutor executor;
	@Captor private ArgumentCaptor<List<String>> captor;

	@Before
	public void before() {
		aligner = new ZipAligner(PATH_TO_ZIPALIGNER, executor);
	}

	@Test
	public void align_executes_expected_command() {
		aligner.align(new File(PATH_TO_APK), PATH_TO_OUTPUT);
		verify(executor).execute(captor.capture());

		assertEquals(1, captor.getAllValues().size());
		assertEquals("/path/to/zipaligner -f 4 /path/to/apk /path/to/output", Strings.join(captor.getValue(), " "));
	}

	@Test(expected = RuntimeException.class)
	public void align_throws_if_executor_fails() {
		when(executor.execute(anyList())).thenThrow(RuntimeException.class);
		aligner.align(new File(PATH_TO_APK), PATH_TO_OUTPUT);
	}
}
