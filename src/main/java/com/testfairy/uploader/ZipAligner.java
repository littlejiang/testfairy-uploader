package com.testfairy.uploader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

class ZipAligner {
	private final String zipAlignPath;
	private final ProcessExecutor executor;

	public ZipAligner(
		String zipAlignPath,
		ProcessExecutor executor
	) {
		this.zipAlignPath = zipAlignPath;
		this.executor = executor;
	}

	void align(File apkPath, String output) {
		try {
			List<String> commands = Arrays.asList(zipAlignPath, "-f", "4", apkPath.getAbsolutePath(), output);
			executor.execute(commands);
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}

	@Override
	public String toString() {
		return "ZipAligner{" +
			"zipAlignPath='" + zipAlignPath + '\'' +
			'}';
	}
}
