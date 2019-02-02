package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Histogram;

import java.util.Random;

public class HistogramExample {
    public static void main(String[] args) {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        Histogram requestLatency = Histogram.build()
                .name("request_latency")
                .help("Time spent in a request")
                .buckets(250, 1000)
                .register(registry);

        int N = 10000;
        for (int i = 0; i < N; i++) {
            Random r = new Random();
            Double v = 200.0 + r.nextInt(100);
            if (r.nextInt(100) > 95) {
                v += 1000;
            }
            requestLatency.observe(v);
        }

        CollectorUtils.print(System.out, registry);
    }
}
