package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Summary;
import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

import static org.junit.Assert.*;

public class QuantileTest {
    @Test
    public void testSummaryQuantile() {
        CollectorRegistry registry = new CollectorRegistry();

        Summary requestLatency = Summary.build()
                .name("request_latency")
                .help("Time spent in a request")
                .quantile(0.5, 0.0)  // 50th percentile
                .quantile(0.95, 0.0) // 95th percentile
                .register(registry);

        int N = 100;
        Double[] xs = new Double[N];
        for (int i = 0; i < N; i++) {
            Random r = new Random();
            Double v = 200.0 + r.nextInt(100);
            if (r.nextInt(10) > 8) {
                v += 1000;
            }
            xs[i] = v;
            requestLatency.observe(v);
        }
        Arrays.sort(xs);

        assertEquals(xs[49], registry.getSampleValue(
                "request_latency", new String[]{"quantile"}, new String[]{"0.5"}));
        assertEquals(xs[94], registry.getSampleValue(
                "request_latency", new String[]{"quantile"}, new String[]{"0.95"}));
    }
}
