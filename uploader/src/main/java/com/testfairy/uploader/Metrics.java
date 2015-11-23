package com.testfairy.uploader;

import java.util.*;

public class Metrics {
    public static final Metrics DEFAULT_METRICS = new Metrics.Builder().addCpu().addMemory().addLogcat().build();

    private final List<String> metrics;

    Metrics(List<String> metrics) {
        this.metrics = metrics;
    }

    String asFormString() {
        return Strings.join(metrics, ",");
    }

    @Override
    public String toString() {
        return "Metrics{" +
            "metrics=" + Strings.join(metrics, ",") +
            '}';
    }

    public static class Builder {
        private Set<String> metrics = new LinkedHashSet<String>(10);

        public Builder addCpu() {
            metrics.add("cpu");
            return this;
        }

        public Builder addMemory() {
            metrics.add("memory");
            return this;
        }

        public Builder addNetwork() {
            metrics.add("network");
            return this;
        }

        public Builder addPhoneSignal() {
            metrics.add("phone-signal");
            return this;
        }

        public Builder addLogcat() {
            metrics.add("logcat");
            return this;
        }

        public Builder addGps() {
            metrics.add("gps");
            return this;
        }

        public Builder addBattery() {
            metrics.add("battery");
            return this;
        }

        public Builder addMic() {
            metrics.add("mic");
            return this;
        }

        public Builder addWifi() {
            metrics.add("wifi");
            return this;
        }

        public Builder addOpenGl() {
            metrics.add("opengl");
            return this;
        }

        public Metrics build() {
            return new Metrics(new ArrayList<String>(metrics));
        }
    }
}
