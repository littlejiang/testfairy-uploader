package com.testfairy.uploader;

import java.io.File;

import static com.testfairy.uploader.Options.optional;

public class AndroidUploader implements Uploader {
	private final TestFairyService service;
	private final AndroidAppSigner signer;
	private final String apiKey;
	private final String apkPath;
	private final String proguardMapPath;
	private final boolean enableInstrumentation;
	private final Options options;
	private final Metrics metrics;

	AndroidUploader(
		TestFairyService service,
		AndroidAppSigner signer,
		String apiKey,
		String apkPath,
		String proguardMapPath,
		boolean enableInstrumentation,
		Options options,
	    Metrics metrics
	) {
		this.service = service;
		this.signer = signer;
		this.apiKey = apiKey;
		this.apkPath = apkPath;
		this.proguardMapPath = proguardMapPath;
		this.enableInstrumentation = enableInstrumentation;
		this.options = options;
		this.metrics = metrics;
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
		if (listener != null) listener.onProgress(0f);
		TestFairyService.Request request = service.newRequest();

		request
			.addString("api_key", apiKey)
			.addFile("apk_file", new File(apkPath))
			.addString("instrumentation", enableInstrumentation ? "on" : "off");

		if (! Strings.isEmpty(proguardMapPath))
			request.addFile("proguard_file", new File(proguardMapPath));

		if (this.metrics != null) {
			request.addString("metrics", metrics.asFormString());
		}

		// Pre-signing
		if (options != null) {
			request
				.addString("max-duration", options.maxDuration)
				.addString("comment", options.comment)
				.addString("video", options.videoRecording)
				.addString("video-quality", options.videoQuality)
				.addString("video-rate", options.framesPerSecond)
				.addString("icon-watermark", value(options.watermarkIcon, "on", "off"))
				.addString("changelog", options.changelog)
				.addString("options", optional(options))
				.addString("screenshot-interval", options.screenInterval)
				.addString("advanced-options", options.advancedOptions)
				.addString("custom", options.customFields);
		}

		if (listener != null) listener.onProgress(33f);
		Build response = request.upload();
		if (listener != null) listener.onProgress(34f);
		File signed = signer.resign(response);
		if (listener != null) listener.onProgress(66f);

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
				.addString("notify", value(options.notify, "on", "off"))
				.addString("auto-update", value(options.autoUpdate, "on", "off"));
		}
		if (listener != null) listener.onProgress(67f);
		response = request.uploadSigned();
		if (listener != null) listener.onProgress(100f);
		signed.delete();

		if (listener != null) listener.onUploadComplete(response);
	}

	private void withoutInstrumentation(Listener listener) {
		if (listener != null) listener.onUploadStarted();
		if (listener != null) listener.onProgress(0f);
		TestFairyService.Request request = service.newRequest();

		request
			.addString("api_key", apiKey)
			.addFile("apk_file", new File(apkPath))
			.addString("instrumentation", enableInstrumentation ? "on" : "off");

		if (! Strings.isEmpty(proguardMapPath))
			request.addFile("proguard_file", new File(proguardMapPath));

		if (this.metrics != null) {
			request.addString("metrics", metrics.asFormString());
		}

		if (options != null) {
			request
				.addString("max-duration", options.maxDuration)
				.addString("comment", options.comment)
				.addString("video", options.videoRecording)
				.addString("video-quality", options.videoQuality)
				.addString("video-rate", options.framesPerSecond)
				.addString("icon-watermark", value(options.watermarkIcon, "on", "off"))
				.addString("testers-groups", options.testers)
				.addString("notify", value(options.notify, "on", "off"))
				.addString("auto-update", value(options.autoUpdate, "on", "off"))
				.addString("changelog", options.changelog)
				.addString("options", optional(options))
				.addString("screenshot-interval", options.screenInterval)
				.addString("advanced-options", options.advancedOptions)
				.addString("custom", options.customFields);
		}

		if (listener != null) listener.onProgress(50f);
		Build response = request.upload();
		if (listener != null) listener.onProgress(100f);

		if (listener != null) listener.onUploadComplete(response);
	}

	private static String value(Boolean flag, String yes, String no) {
		if (flag == null)
			return null;

		return flag ? yes : no;
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
			", metrics=" + metrics +
			'}';
	}

	public static class Builder {
		private final String apiKey;

		private Options options;
		private Metrics metrics;
		private String keystorePath;
		private String storePassword;
		private String keyPassword;
		private String keyStoreAlias;
		private String digestAlgorithm;
		private String signatureAlgorithm;
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

		public Builder setMetrics(Metrics metrics) {
			this.metrics = metrics;
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

		public Builder setJarSignerPath(String jarSignerPath) {
			this.jarSignerPath = jarSignerPath;
			return this;
		}

		public void setHttpUserAgent(String httpUserAgent) {
			this.httpUserAgent = httpUserAgent;
			return this;
		}

		public AndroidUploader build() {
			if (Strings.isEmpty(apiKey)) throw new IllegalArgumentException("API Key is empty. Please go to https://app.testfairy.com/settings/ and use the API Key found there");
			if (Strings.isEmpty(apkPath)) throw new IllegalArgumentException("Path to APK is empty. Call setApkPath with a valid path to APK.");
			if (! new File(apkPath).exists()) throw new IllegalArgumentException("APK was not found at " + apkPath);
			if (! Strings.isEmpty(proguardMapPath) && ! new File(proguardMapPath).exists()) throw new IllegalArgumentException("Proguard map file was not found at " + proguardMapPath);
			if (Strings.isEmpty(httpUserAgent)) httpUserAgent = Config.HTTP_USER_AGENT;
			Options.validateForAndroid(options, enableInstrumentation);
			Metrics.validateForAndroid(metrics);

			TestFairyService service = new TestFairyService(
				Config.SERVER_ENDPOINT,
				httpUserAgent,
				new TestFairyService.ProxyInfo(
					proxyHost, proxyPort, proxyUser, proxyPassword
				)
			);

			if (enableInstrumentation) {
				if (Strings.isEmpty(jarSignerPath)) jarSignerPath = environment.locateJarsigner();

				if (Strings.isEmpty(jarSignerPath)) throw new IllegalArgumentException("Path to jarsigner cannot be null");
				if (Strings.isEmpty(keystorePath)) throw new IllegalArgumentException("Keystore path cannot be empty");
				if (Strings.isEmpty(keyStoreAlias)) throw new IllegalArgumentException("Keystore alias cannot be empty");
				if (Strings.isEmpty(storePassword)) throw new IllegalArgumentException("Store password cannot be empty");
				if (Strings.isEmpty(digestAlgorithm)) throw new IllegalArgumentException("Digest Algorithm cannot be empty");
				if (Strings.isEmpty(signatureAlgorithm)) throw new IllegalArgumentException("Signature Algorithm cannot be empty");

				if (! new File(jarSignerPath).exists()) throw new IllegalArgumentException("jarsigner was not found at " + jarSignerPath);
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
				new ZipAligner()
			);

			return new AndroidUploader(
				service,
				signer,
				this.apiKey,
				this.apkPath,
				this.proguardMapPath,
				this.enableInstrumentation,
				this.options,
				this.metrics
			);
		}
	}
}
