package com.testfairy.uploader;

import java.io.File;

public class AndroidUploader implements Uploader {
	private final TestFairyService service;
	private final String apiKey;
	private final String apkPath;
	private final String proguardMapPath;
	private final Options options;

	AndroidUploader(
		TestFairyService service,
		String apiKey,
		String apkPath,
		String proguardMapPath,
		Options options
	) {
		this.service = service;
		this.apiKey = apiKey;
		this.apkPath = apkPath;
		this.proguardMapPath = proguardMapPath;
		this.options = options;
	}

	@Override
	public void upload(Listener listener) {
		try {
			if (listener != null) listener.onUploadStarted();

			TestFairyService.Request request = service.newRequest();

			request
				.addString("api_key", apiKey)
				.addFile("apk_file", new File(apkPath))
				.addString("changelog", null);  // TODO

			if (! Strings.isEmpty(proguardMapPath))
				request.addFile("proguard_file", new File(proguardMapPath));

			// Pre-signing
			if (options != null) {
				request
					.addString("metrics", options.metrics)
					.addString("max-duration", options.maxDuration)
					.addString("comment", options.comment)
					.addString("video", options.videoRecording)
					.addString("video-quality", options.videoQuality)
					.addString("video-rate", options.framesPerSecond)
					.addString("icon-watermark", options.watermarkIcon ? "on" : "off")
					.addString("record-on-background", null)	// TODO
					.addString("screenshot-interval", null)		// TODO
					.addString("advanced-options", null)		// TODO
					.addString("data-only-wifi", null);			// TODO
			}

			request.upload();
// Signing
//			request = service.newRequest();
//			if (options != null) {
//				request
// 					.addString("testers-groups", options.testers)
// 					.addString("notify", options.notify ? "on" : "off")
//					.addString("auto-update", options.autoUpdate ? "on" : "off");
//				proguard_file
//			}

			if (listener != null) listener.onUploadComplete();
		} catch (Exception exception) {
			exception.printStackTrace();
			if (listener != null) listener.onUploadFailed(exception);
		}
	}

	/*
	ignore instrumentation // is this a parameter? or detected by the uploader? Can I pass a parameter into the curl command?
	*/
	public static class Builder {
		private final String apiKey;
		private Options options;
		private String keystorePath;
		private String keyStorePassword;
		private String keyStoreAlias;
		private String keyToolPath;
		private String zipPath;
		private String unzipPath;
		private String zipAlignPath;
		private String jarSignerPath;
		private boolean ignoreInstrumentation;

		private String apkPath;
		private String proguardMapPath;

		private String proxyHost;
		private int proxyPort;
		private String proxyUser;
		private String proxyPassword;

		public Builder(String apiKey) {
			this.apiKey = apiKey;
			this.ignoreInstrumentation = false;
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

		public Builder setApkPath(String apkPath) {
			this.apkPath = apkPath;
			return this;
		}

		public Builder setProguardMapPath(String proguardMapPath) {
			this.proguardMapPath = proguardMapPath;
			return this;
		}

		public Builder setKeystore(String path, String alias, String password) {
			this.keystorePath = path;
			this.keyStoreAlias = alias;
			this.keyStorePassword = password;

			return this;
		}

		public Builder setZipPath(String zipPath) {
			this.zipPath = zipPath;
			return this;
		}

		public Builder setUnzipPath(String unzipPath) {
			this.unzipPath = unzipPath;
			return this;
		}

		public Builder setZipAlignPath(String zipAlignPath) {
			this.zipAlignPath = zipAlignPath;
			return this;
		}

		public Builder setJarSignerPath(String jarSignerPath) {
			this.jarSignerPath = jarSignerPath;
			return this;
		}

		public Builder setKeyToolPath(String keyToolPath) {
			this.keyToolPath = keyToolPath;
			return this;
		}

		public AndroidUploader build() {
			if (Strings.isEmpty(apkPath)) throw new IllegalArgumentException("Path to APK is null. Call setApkPath with a valid path to APK.");

			return new AndroidUploader(
				new TestFairyService(
					Config.SERVER_ENDPOINT,
					Config.HTTP_USER_AGENT,
					new TestFairyService.ProxyInfo(
						proxyHost, proxyPort, proxyUser, proxyPassword
					)
				),
				this.apiKey,
				this.apkPath,
				this.proguardMapPath,
				this.options
			);
		}
	}
}
