package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Summary;

public class SummaryExample {
    public static void main(String[] args) {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        Summary processedRecords = Summary.build()
                .name("processed_records")
                .help("Number of processed records")
                .labelNames("channel")
                .quantile(0.5, 0.0) // median
                .register(registry);

        processedRecords.labels("foo").observe(10);
        processedRecords.labels("bar").observe(234);
        processedRecords.labels("bar").observe(1000);
        processedRecords.labels("baz").observe(345);
        processedRecords.labels("foo").observe(12);
        processedRecords.labels("foo").observe(8);
        processedRecords.labels("foo").observe(10);
        processedRecords.labels("foo").observe(10);
        processedRecords.labels("foo").observe(9);
        processedRecords.labels("foo").observe(8);

        CollectorUtils.print(System.out, registry);
    }
}
