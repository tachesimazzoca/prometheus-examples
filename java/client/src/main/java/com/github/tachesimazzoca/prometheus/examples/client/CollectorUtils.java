package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.Collector;
import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.common.TextFormat;

import java.io.Closeable;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;

public class CollectorUtils {

    public static void print(OutputStream out, CollectorRegistry registry) {
        Enumeration<Collector.MetricFamilySamples> metrics = registry.metricFamilySamples();
        Writer w = new OutputStreamWriter(out);
        try {
            TextFormat.write004(w, metrics);
            w.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            closeQuietly(w);
        }
    }

    private static void closeQuietly(Closeable obj) {
        try {
            if (obj != null) {
                obj.close();
            }
        } catch (Exception e) {
        }
    }
}
