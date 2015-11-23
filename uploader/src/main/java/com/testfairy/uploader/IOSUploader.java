package com.testfairy.uploader;

import java.io.File;

public class IOSUploader implements Uploader {
	private final TestFairyService service;
	private final String apiKey;
	private final String ipaPath;
	private final String symbolsMapPath;
	private final Options options;

	IOSUploader(
		TestFairyService service,
		String apiKey,
		String ipaPath,
		String symbolsMapPath,
		Options options
	) {
		this.service = service;
		this.apiKey = apiKey;
		this.ipaPath = ipaPath;
		this.symbolsMapPath = symbolsMapPath;
		this.options = options;
	}

	@Override
	public void upload(Listener listener) {
		try {
			if (listener != null) listener.onUploadStarted();

			TestFairyService.Request request = service.newRequest();

			request
				.addString("api_key", apiKey)
				.addFile("file", new File(ipaPath))
				.addString("changelog", null);                                      // TODO

			if (! Strings.isEmpty(symbolsMapPath))
				request.addFile("symbols_file", new File(symbolsMapPath));

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
					.addString("auto-update", options.autoUpdate ? "on" : "off")
					.addString("record-on-background", null)                        // TODO
					.addString("screenshot-interval", null)                         // TODO: Is this different from video-rate
					.addString("advanced-options", null)                            // TODO
					.addString("data-only-wifi", null)                              // TODO
					.addString("shake", null)                                       // TODO
					.addString("video-only-wifi", null);                            // TODO
			}

			Build response = request.upload();

			if (listener != null) listener.onUploadComplete(response);
		} catch (Exception exception) {
			if (listener != null) listener.onUploadFailed(exception);
		}
	}

	@Override
	public String toString() {
		return "IOSUploader{" +
			"service=" + service +
			", apiKey='" + apiKey + '\'' +
			", ipaPath='" + ipaPath + '\'' +
			", symbolsMapPath='" + symbolsMapPath + '\'' +
			", options=" + options +
			'}';
	}

	public static class Builder {
		private final String apiKey;
		private Options options;
		private String httpUserAgent;

		private String ipaPath;
		private String symbolsPath;

		private String proxyHost;
		private int proxyPort;
		private String proxyUser;
		private String proxyPassword;

		public Builder(String apiKey) {
			this.apiKey = apiKey;
			this.proxyPort = -1;
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

		public Builder setIpaPath(String ipaPath) {
			this.ipaPath = ipaPath;
			return this;
		}

		public Builder setSymbolsPath(String symbolsPath) {
			this.symbolsPath = symbolsPath;
			return this;
		}

		public Builder setHttpUserAgent(String userAgent) {
			this.httpUserAgent = userAgent;
			return this;
		}

		public IOSUploader build() {
			if (Strings.isEmpty(apiKey)) throw new IllegalArgumentException("API Key is empty. Please goto to https://app.testfairy.com/settings/ and use the API Key found there");
			if (Strings.isEmpty(ipaPath)) throw new IllegalArgumentException("Path to IPA not set. Call setIpaPath with path to IPA.");
			if (! new File(ipaPath).exists()) throw new IllegalArgumentException("IPA was not found at " + ipaPath);
			if (! Strings.isEmpty(symbolsPath) && ! new File(symbolsPath).exists()) throw new IllegalArgumentException("Symbols file was not found at " + symbolsPath);
			if (Strings.isEmpty(httpUserAgent)) httpUserAgent = Config.HTTP_USER_AGENT;

			return new IOSUploader(
				new TestFairyService(
					Config.SERVER_ENDPOINT,
					httpUserAgent,
					new TestFairyService.ProxyInfo(
						proxyHost, proxyPort, proxyUser, proxyPassword
					)
				),
				apiKey,
				ipaPath,
				symbolsPath,
				options
			);
		}
	}
}
