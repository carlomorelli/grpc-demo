package com.csoft.grpcdemo.vertx;

import io.vertx.core.Vertx;
import io.vertx.grpc.VertxServer;
import io.vertx.grpc.VertxServerBuilder;

public class VertxGrpcServer {

    public static void main(String... args) {

        Vertx vertx = Vertx.vertx();

        VertxServer server = VertxServerBuilder
                .forAddress(vertx, "localhost", 50001)
                .addService(new LicenseDataServiceImpl())
                .build();

        server.start(asyncResult -> {
            if (asyncResult.succeeded()) {
                System.out.println("[Vert.x server] started on port 50001.");
            } else {
                asyncResult.cause().printStackTrace();
                System.exit(1);
            }
        });
    }
}