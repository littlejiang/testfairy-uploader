package com.testfairy.uploader;

import java.io.File;

public class IosUploader implements Uploader {
	private final TestFairyService service;
	private final String apiKey;
	private final Options options;

	IosUploader(
		TestFairyService service,
		String apiKey,
		Options options
	) {
		this.service = service;
		this.apiKey = apiKey;
		this.options = options;
	}

	@Override
	public void upload(String pathToIpa, Listener listener) {
		try {
			if (listener != null) listener.onUploadStarted();

			TestFairyService.Request request = service.newRequest();

			request
				.addString("api_key", apiKey)
				.addFile("file", new File(pathToIpa));
//			request.addString("changelog", changelog);

			if (options != null) {
				request
					.addString("metrics", options.metrics)
					.addString("max-duration", options.maxDuration)
					.addString("comment", options.comment)
					.addString("video", options.videoRecording)
					.addString("video-quality", options.videoQuality)
					.addString("video-rate", options.framesPerSecond)
					.addString("icon-watermark", options.watermarkIcon ? "on" : "off")
					.addString("testers-groups", options.testers)
					.addString("notify", options.notify ? "on" : "off")
					.addString("auto-update", options.autoUpdate ? "on" : "off");
				// record-on-background
				// screenshot-interval
				// advanced-options
				// data-only-wifi
			}

			request.upload();

			if (listener != null) listener.onUploadComplete();
		} catch (Exception exception) {
			if (listener != null) listener.onUploadFailed(exception);
		}
	}

/*
symbols file
proxy
*/
	public static class Builder {
		private final String apiKey;
		private Options options;

		private String proxyHost;
		private int proxyPort;
		private String proxyUser;
		private String proxyPassword;

		public Builder(String apiKey) {
			this.apiKey = apiKey;
		}

		public Builder setOptions(Options options) {
			this.options = options;
			return this;
		}

		public Builder setProxyHost(String host, int port) {
			this.proxyHost = host;
			this.proxyPort = port;
			return this;
		}

		public Builder setProxyCredentials(String user, String password) {
			this.proxyUser = user;
			this.proxyPassword = password;
			return this;
		}

		public IosUploader build() {
//			String serverEndpoint = "http://app.testfairy.com";
			String serverEndpoint = "http://free.vijay.ca.dev.testfairy.net";

			return new IosUploader(
				new TestFairyService(
					serverEndpoint,
					new TestFairyService.ProxyInfo(
						proxyHost, proxyPort, proxyUser, proxyPassword
					)
				),
				apiKey,
				options
			);
		}
	}
}
