package com.testfairy.uploader;

import java.util.*;

public class Metrics {
	public static final Metrics DEFAULT_METRICS = new Metrics.Builder().addCpu().addMemory().addLogcat().build();

	private static final String CPU = "cpu";
	private static final String MEMORY = "memory";
	private static final String NETWORK = "network";
	private static final String PHONE_SIGNAL = "phone-signal";
	private static final String LOGCAT = "logcat";
	private static final String GPS = "gps";
	private static final String BATTERY = "battery";
	private static final String MIC = "mic";
	private static final String WIFI = "wifi";
	private static final String OPENGL = "opengl";
	private static final List<String> SUPPORTED_ANROID_METRICS = Arrays.asList(
		CPU, MEMORY, NETWORK, PHONE_SIGNAL, LOGCAT, GPS, BATTERY, MIC, WIFI, OPENGL
	);
	private static final List<String> SUPPORTED_IOS_METRICS = Arrays.asList(
		CPU, MEMORY, LOGCAT
	);

	private final List<String> metrics;

	Metrics(List<String> metrics) {
		this.metrics = metrics;
	}

	String asFormString() {
		return Strings.join(metrics, ",");
	}

	static void validateForAndroid(Metrics metrics) {
		validateSupport(metrics, "Android", SUPPORTED_ANROID_METRICS);
	}

	static void validateForIOS(Metrics metrics) {
		validateSupport(metrics, "iOS", SUPPORTED_IOS_METRICS);
	}

	private static void validateSupport(Metrics metrics, String platform, List<String> supported) {
		if (metrics == null) return;

		for (String metric : metrics.metrics) {
			if (! supported.contains(metric))
				throw new IllegalStateException(metric + " is not supported when uploading " + platform + " builds");
		}
	}

	@Override
	public String toString() {
		return "Metrics{" +
			"metrics=" + Strings.join(metrics, ",") +
			'}';
	}

	public static class Builder {
		private Set<String> metrics = new LinkedHashSet<String>(10);

		public Builder addAll(String all) {
			if (Strings.isEmpty(all))
				return this;

			for (String metric : all.split(",")) {
				if (! Strings.isEmpty(metric))
					metrics.add(metric.trim());
			}

			return this;
		}

		public Builder addCpu() {
			metrics.add(CPU);
			return this;
		}

		public Builder addMemory() {
			metrics.add(MEMORY);
			return this;
		}

		public Builder addNetwork() {
			metrics.add(NETWORK);
			return this;
		}

		public Builder addPhoneSignal() {
			metrics.add(PHONE_SIGNAL);
			return this;
		}

		public Builder addLogcat() {
			metrics.add(LOGCAT);
			return this;
		}

		public Builder addGps() {
			metrics.add(GPS);
			return this;
		}

		public Builder addBattery() {
			metrics.add(BATTERY);
			return this;
		}

		public Builder addMic() {
			metrics.add(MIC);
			return this;
		}

		public Builder addWifi() {
			metrics.add(WIFI);
			return this;
		}

		public Builder addOpenGl() {
			metrics.add(OPENGL);
			return this;
		}

		public Metrics build() {
			return new Metrics(new ArrayList<String>(metrics));
		}
	}
}
