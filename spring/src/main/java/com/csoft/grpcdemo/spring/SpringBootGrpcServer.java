package com.csoft.grpcdemo.spring;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringBootGrpcServer {

    public static void main(String... args) {
        // runs on port 50002
        SpringApplication.run(SpringBootGrpcServer.class, args);
    }


}
