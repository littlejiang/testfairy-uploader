package com.testfairy.uploader;

import java.io.File;

public class AndroidUploader implements Uploader {
	private final TestFairyService service;
	private final AndroidAppSigner signer;
	private final String apiKey;
	private final String apkPath;
	private final String proguardMapPath;
	private final boolean enableInstrumentation;
	private final Options options;

	AndroidUploader(
		TestFairyService service,
		AndroidAppSigner signer,
		String apiKey,
		String apkPath,
		String proguardMapPath,
		boolean enableInstrumentation,
		Options options
	) {
		this.service = service;
		this.signer = signer;
		this.apiKey = apiKey;
		this.apkPath = apkPath;
		this.proguardMapPath = proguardMapPath;
		this.enableInstrumentation = enableInstrumentation;
		this.options = options;
	}

	@Override
	public void upload(Listener listener) {
		try {
			if (enableInstrumentation) {
				withInstrumentation(listener);
			} else {
				withoutInstrumentation(listener);
			}
		} catch (Exception exception) {
			exception.printStackTrace();
			if (listener != null) listener.onUploadFailed(exception);
		}
	}

	private void withInstrumentation(Listener listener) {
		if (listener != null) listener.onUploadStarted();

		TestFairyService.Request request = service.newRequest();

		request
			.addString("api_key", apiKey)
			.addFile("apk_file", new File(apkPath))
			.addString("changelog", null)                                              // TODO
			.addString("instrumentation", enableInstrumentation ? "on" : "off");

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
				.addString("record-on-background", null)                                // TODO
				.addString("screenshot-interval", null)                                 // TODO
				.addString("advanced-options", null)                                    // TODO
				.addString("data-only-wifi", null)                                      // TODO
				.addString("shake", null)                                               // TODO
				.addString("video-only-wifi", null);                                    // TODO
		}

		Build response = request.upload();
		File signed = signer.resign(response);

		// Signing
		request = service.newRequest();
		request
			.addString("api_key", apiKey)
			.addFile("apk_file", signed);

		if (! Strings.isEmpty(proguardMapPath))
			request.addFile("proguard_file", new File(proguardMapPath));

		if (options != null) {
			request
				.addString("testers-groups", options.testers)
				.addString("notify", options.notify ? "on" : "off")
				.addString("auto-update", options.autoUpdate ? "on" : "off");
		}
		response = request.uploadSigned();
		signed.delete();

		if (listener != null) listener.onUploadComplete(response);
	}

	private void withoutInstrumentation(Listener listener) {
		if (listener != null) listener.onUploadStarted();
		TestFairyService.Request request = service.newRequest();

		request
			.addString("api_key", apiKey)
			.addFile("apk_file", new File(apkPath))
			.addString("changelog", null)                                              // TODO
			.addString("instrumentation", enableInstrumentation ? "on" : "off");

		if (! Strings.isEmpty(proguardMapPath))
			request.addFile("proguard_file", new File(proguardMapPath));

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
				.addString("record-on-background", null)                                // TODO
				.addString("screenshot-interval", null)                                 // TODO
				.addString("advanced-options", null)                                    // TODO
				.addString("data-only-wifi", null)                                      // TODO
				.addString("shake", null)                                               // TODO
				.addString("video-only-wifi", null);                                    // TODO
		}

		Build response = request.upload();

		if (listener != null) listener.onUploadComplete(response);
	}

	@Override
	public String toString() {
		return "AndroidUploader{" +
			"service=" + service +
			", signer=" + signer +
			", apiKey='" + apiKey + '\'' +
			", apkPath='" + apkPath + '\'' +
			", proguardMapPath='" + proguardMapPath + '\'' +
			", enableInstrumentation=" + enableInstrumentation +
			", options=" + options +
			'}';
	}

	public static class Builder {
		private final String apiKey;

		private Options options;
		private String keystorePath;
		private String storePassword;
		private String keyPassword;
		private String keyStoreAlias;
		private String digestAlgorithm;
		private String signatureAlgorithm;
		private String zipAlignPath;
		private String jarSignerPath;
		private boolean enableInstrumentation;

		private String apkPath;
		private String proguardMapPath;
		private String httpUserAgent;

		private String proxyHost;
		private int proxyPort;
		private String proxyUser;
		private String proxyPassword;

		private SdkEnvironment environment;

		public Builder(String apiKey) {
			this.apiKey = apiKey;
			this.enableInstrumentation = true;
			this.proxyPort = -1;
			this.digestAlgorithm = "SHA1";
			this.signatureAlgorithm = "MD5withRSA";
			this.environment = new SdkEnvironment();
		}

		public Builder enableInstrumentation(boolean enable) {
			this.enableInstrumentation = enable;
			return this;
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

		public Builder setKeystore(
			String path,
			String alias,
			String storePassword,
			String keyPassword
		) {
			this.keystorePath = path;
			this.keyStoreAlias = alias;
			this.keyPassword = keyPassword;
			this.storePassword = storePassword;

			return this;
		}

		public Builder setDigestAlgorithm(String digestAlgorithm) {
			this.digestAlgorithm = digestAlgorithm;
			return this;
		}

		public Builder setSignatureAlgorithm(String signatureAlgorithm) {
			this.signatureAlgorithm = signatureAlgorithm;
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

		public void setHttpUserAgent(String httpUserAgent) {
			this.httpUserAgent = httpUserAgent;
		}

		public AndroidUploader build() {
			if (Strings.isEmpty(apiKey)) throw new IllegalArgumentException("API Key is empty. Please goto to https://app.testfairy.com/settings/ and use the API Key found there");
			if (Strings.isEmpty(apkPath)) throw new IllegalArgumentException("Path to APK is empty. Call setApkPath with a valid path to APK.");
			if (! new File(apkPath).exists()) throw new IllegalArgumentException("APK was not found at " + apkPath);
			if (! Strings.isEmpty(proguardMapPath) && ! new File(proguardMapPath).exists()) throw new IllegalArgumentException("Proguard map file was not found at " + proguardMapPath);
			if (Strings.isEmpty(httpUserAgent)) httpUserAgent = Config.HTTP_USER_AGENT;

			TestFairyService service = new TestFairyService(
				Config.SERVER_ENDPOINT,
				httpUserAgent,
				new TestFairyService.ProxyInfo(
					proxyHost, proxyPort, proxyUser, proxyPassword
				)
			);

			if (Strings.isEmpty(jarSignerPath)) jarSignerPath = environment.locateJarsigner();
			if (Strings.isEmpty(zipAlignPath)) zipAlignPath = environment.locateZipalign();

			if (enableInstrumentation) {
				if (Strings.isEmpty(jarSignerPath)) throw new IllegalArgumentException("Path to jarsigner cannot be null");
				if (Strings.isEmpty(zipAlignPath)) throw new IllegalArgumentException("Path to zipalign cannot be null");
				if (Strings.isEmpty(keystorePath)) throw new IllegalArgumentException("Keystore path cannot be empty");
				if (Strings.isEmpty(keyStoreAlias)) throw new IllegalArgumentException("Keystore alias cannot be empty");
				if (Strings.isEmpty(storePassword)) throw new IllegalArgumentException("Store password cannot be empty");
				if (Strings.isEmpty(digestAlgorithm)) throw new IllegalArgumentException("Digest Algorithm cannot be empty");
				if (Strings.isEmpty(signatureAlgorithm)) throw new IllegalArgumentException("Signature Algorithm cannot be empty");

				if (! new File(jarSignerPath).exists()) throw new IllegalArgumentException("jarsigner was not found at " + jarSignerPath);
				if (! new File(zipAlignPath).exists()) throw new IllegalArgumentException("zipAlignPath was not found at " + zipAlignPath);
				if (! new File(keystorePath).exists()) throw new IllegalArgumentException("Keystore was not found at " + keystorePath);
			}

			ProcessExecutor executor = new ProcessExecutor();
			AndroidAppSigner signer = new AndroidAppSigner(
				this.apiKey,
				this.apkPath,
				service,
				new JarSigner(
					jarSignerPath,
					keystorePath,
					keyStoreAlias,
					storePassword,
					keyPassword,
					digestAlgorithm,
					signatureAlgorithm,
					executor
				),
				new ZipAligner(zipAlignPath, executor)
			);

			return new AndroidUploader(
				service,
				signer,
				this.apiKey,
				this.apkPath,
				this.proguardMapPath,
				this.enableInstrumentation,
				this.options
			);
		}
	}
}
