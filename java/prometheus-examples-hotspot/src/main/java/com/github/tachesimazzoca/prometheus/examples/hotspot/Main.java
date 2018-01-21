package com.github.tachesimazzoca.prometheus.examples.hotspot;

import io.prometheus.client.CollectorRegistry;
import io.prometheus.client.exporter.HTTPServer;
import io.prometheus.client.hotspot.DefaultExports;

import java.net.InetSocketAddress;

public class Main {

    private static final int DEFAULT_PORT = 9090;

    public static void main(String[] args) {
        int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = DEFAULT_PORT;
        }

        try {
            // Register all the hotspot collectors
            // See https://github.com/prometheus/client_java/tree/master/simpleclient_hotspot
            DefaultExports.initialize();

            HTTPServer server = new HTTPServer(
                    new InetSocketAddress(port), CollectorRegistry.defaultRegistry, false);

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
