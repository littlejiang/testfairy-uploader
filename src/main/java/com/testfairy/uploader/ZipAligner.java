package com.testfairy.uploader;

import java.io.File;
import java.util.Arrays;
import java.util.List;

class ZipAligner {
	void align(File apkPath, File output) {
		try {
			new ZipAlign.ZipAligner(apkPath, output).run();
		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
}
