package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Counter;
import io.prometheus.client.Gauge;

public class GaugeExample {
    public static void main(String[] args) {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        Gauge runningProcesses = Gauge.build()
                .name("running_processes")
                .help("Number of running processes")
                .labelNames("node")
                .register(registry);

        runningProcesses.labels("ap-1").inc();
        runningProcesses.labels("ap-1").inc();
        runningProcesses.labels("ap-2").inc();
        runningProcesses.labels("ap-1").dec();
        runningProcesses.labels("ap-2").inc();

        CollectorUtils.print(System.out, registry);
    }
}
