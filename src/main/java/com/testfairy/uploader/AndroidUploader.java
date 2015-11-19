package com.testfairy.uploader;

import java.io.File;

public class AndroidUploader implements Uploader {
	private final TestFairyService service;
	private final String apiKey;
	private final Options options;

	AndroidUploader(
		TestFairyService service,
		String apiKey,
		Options options
	) {
		this.service = service;
		this.apiKey = apiKey;
		this.options = options;
	}

	@Override
	public void upload(String pathToApk, Listener listener) {
		try {
			if (listener != null) listener.onUploadStarted();

			TestFairyService.Request request = service.newRequest();

			request
				.addString("api_key", apiKey)
				.addFile("apk_file", new File(pathToApk));
//				.addString("proguard_file", pathToProguardMap);
//				.addString("changelog", changelog);

			// Pre-signing
			if (options != null) {
				request
					.addString("metrics", options.metrics)
					.addString("max-duration", options.maxDuration)
					.addString("comment", options.comment)
					.addString("video", options.videoRecording)
					.addString("video-quality", options.videoQuality)
					.addString("video-rate", options.framesPerSecond)
					.addString("icon-watermark", options.watermarkIcon ? "on" : "off");
				// record-on-background
				// screenshot-interval
				// advanced-options
				// data-only-wifi
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
	# Your Keystore, Storepass and Alias, the ones you use to sign your app.
	KEYSTORE=
	STOREPASS=
	ALIAS=
	ZIP=zip
	UNZIP=unzip
	KEYTOOL=keytool
	ZIPALIGN=zipalign
	JARSIGNER=jarsigner
	proguard-mapping=/mapping.txt
	proxy
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
		private String proguardMapPath;
		private boolean ignoreInstrumentation;

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
			return new AndroidUploader(
				new TestFairyService(
					Config.SERVER_ENDPOINT,
					Config.HTTP_USER_AGENT,
					new TestFairyService.ProxyInfo(
						proxyHost, proxyPort, proxyUser, proxyPassword
					)
				),
				this.apiKey,
				this.options
			);
		}
	}
}
