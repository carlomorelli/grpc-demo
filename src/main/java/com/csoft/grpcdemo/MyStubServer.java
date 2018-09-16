package com.csoft.grpcdemo;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;

/**
 * Hello world!
 */
public class MyStubServer {
    public static void main(String... args) throws IOException, InterruptedException {

        // configure and start
        Server server = ServerBuilder.forPort(50000)
//                    .addService(new LicenseDataServiceImpl())
                .build();
        server.start();

        // add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("[Stub server] Received shutdown signal...");
            server.shutdown();
        }));

        // Server threads are running in the background.
        System.out.println("[Stub server] started on port 50000.");
        server.awaitTermination();
    }
}
