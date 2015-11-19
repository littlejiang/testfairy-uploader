package com.testfairy.uploader;

public interface Uploader {
	void upload(String pathToArtifact, Listener listener);
}
