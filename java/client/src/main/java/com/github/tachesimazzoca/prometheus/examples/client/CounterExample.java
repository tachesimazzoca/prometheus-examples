package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;

public class CounterExample {
    public static void main(String[] args) {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        Counter requestsTotal = io.prometheus.client.Counter.build()
                .name("api_requests_total")
                .help("Number of API requests")
                .labelNames("url", "method")
                .register(registry);

        requestsTotal.labels("/articles", "GET").inc();
        requestsTotal.labels("/articles", "POST").inc();
        requestsTotal.labels("/dashboard", "GET").inc();
        requestsTotal.labels("/articles", "POST").inc();
        requestsTotal.labels("/articles", "GET").inc();
        requestsTotal.labels("/articles", "GET").inc();

        CollectorUtils.print(System.out, registry);
    }
}
