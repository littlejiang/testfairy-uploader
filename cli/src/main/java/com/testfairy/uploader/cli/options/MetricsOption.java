package com.testfairy.uploader.cli.options;

import com.testfairy.uploader.Metrics;
import com.testfairy.uploader.Options;
import com.testfairy.uploader.cli.StringUtils;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;

public class MetricsOption implements OptionsArg {

    private OptionSpec<String> metrics;

    @Override
    public void configure(OptionParser parser) {
        this.metrics = parser.accepts("metrics", "Comma-separated list of metrics to record").withRequiredArg();
    }

    @Override
    public Options.Builder apply(OptionSet optionSet, Options.Builder builder) {
        if (! optionSet.has(this.metrics))
            return builder;

        String all = optionSet.valueOf(this.metrics);
        if (StringUtils.isEmpty(all))
            return builder;

        Metrics.Builder metrics = null;
        for (String metric : all.split(",")) {
            if ("cpu".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addCpu() : metrics.addCpu();
            } else if ("opengl".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addOpenGl() : metrics.addOpenGl();
            } else if ("memory".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addMemory() : metrics.addMemory();
            } else if ("network".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addNetwork() : metrics.addNetwork();
            } else if ("phone-signal".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addPhoneSignal() : metrics.addPhoneSignal();
            } else if ("logcat".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addLogcat() : metrics.addLogcat();
            } else if ("gps".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addGps() : metrics.addGps();
            } else if ("battery".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addBattery() : metrics.addBattery();
            } else if ("mic".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addMic() : metrics.addMic();
            } else if ("wifi".equals(metric)) {
                metrics = (metrics == null) ? new Metrics.Builder().addWifi() : metrics.addWifi();
            }
        }

        if (metrics == null)
            return builder;


        return builder == null ? new Options.Builder().setMetrics(metrics.build()) : builder.setMetrics(metrics.build());
    }
}
