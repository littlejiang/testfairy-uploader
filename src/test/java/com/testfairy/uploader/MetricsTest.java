package com.testfairy.uploader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public class MetricsTest {
    @Test
    public void default_has_expected_values() {
        assertEquals("cpu,memory,logcat", Metrics.DEFAULT_METRICS.asFormString());
    }

    @Test
    public void new_metrics_has_adds_no_values() {
        Metrics metrics = new Metrics.Builder().build();
        assertEquals("", metrics.asFormString());
    }

    @Test
    public void can_only_add_metric_value_once() {
        Metrics metrics = new Metrics.Builder()
            .addCpu()
            .addCpu()
            .build();
        assertEquals("cpu", metrics.asFormString());
    }

    @Test
    public void can_add_cpu_metric() {
        Metrics metrics = new Metrics.Builder()
            .addCpu()
            .build();
        assertEquals("cpu", metrics.asFormString());
    }

    @Test
    public void can_add_memory_metric() {
        Metrics metrics = new Metrics.Builder()
            .addMemory()
            .build();
        assertEquals("memory", metrics.asFormString());
    }

    @Test
    public void can_add_network_metric() {
        Metrics metrics = new Metrics.Builder()
            .addNetwork()
            .build();
        assertEquals("network", metrics.asFormString());
    }

    @Test
    public void can_add_phone_signal_metric() {
        Metrics metrics = new Metrics.Builder()
            .addPhoneSignal()
            .build();
        assertEquals("phone-signal", metrics.asFormString());
    }

    @Test
    public void can_add_logcat_metric() {
        Metrics metrics = new Metrics.Builder()
            .addLogcat()
            .build();
        assertEquals("logcat", metrics.asFormString());
    }

    @Test
    public void can_add_gps_metric() {
        Metrics metrics = new Metrics.Builder()
            .addGps()
            .build();
        assertEquals("gps", metrics.asFormString());
    }

    @Test
    public void can_add_battery_metric() {
        Metrics metrics = new Metrics.Builder()
            .addBattery()
            .build();
        assertEquals("battery", metrics.asFormString());
    }

    @Test
    public void can_add_mic_metric() {
        Metrics metrics = new Metrics.Builder()
            .addMic()
            .build();

        assertEquals("mic", metrics.asFormString());
    }

    @Test
    public void can_add_wifi_metric() {
        Metrics metrics = new Metrics.Builder()
            .addWifi()
            .build();
        assertEquals("wifi", metrics.asFormString());
    }

    @Test
    public void can_add_opengl_metric() {
        Metrics metrics = new Metrics.Builder()
            .addOpenGl()
            .build();
        assertEquals("opengl", metrics.asFormString());
    }

    @Test
    public void can_add_multiple_metrics() {
        Metrics metrics = new Metrics.Builder()
            .addCpu()
            .addMemory()
            .build();
        assertEquals("cpu,memory", metrics.asFormString());
    }
}
