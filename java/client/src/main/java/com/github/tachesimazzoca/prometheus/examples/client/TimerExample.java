package com.github.tachesimazzoca.prometheus.examples.client;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.Summary;

public class TimerExample {
    public static void main(String[] args) {
        CollectorRegistry registry = CollectorRegistry.defaultRegistry;

        Summary processedTime = Summary.build()
                .name("processed_time")
                .help("Time spent in a process")
                .labelNames("channel")
                .register(registry);

        for (int i = 0; i < 100; i++) {
            Summary.Timer timer = processedTime.labels("foo").startTimer();
            try {
                // Do something to be observed.
                Thread.sleep(1L);
            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                timer.observeDuration();
            }
        }

        for (int i = 0; i < 100; i++) {
            processedTime.labels("bar").time(new Runnable() {
                @Override
                public void run() {
                    try {
                        // Do something to be observed.
                        Thread.sleep(1L);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            });
        }

        CollectorUtils.print(System.out, registry);
    }
}
