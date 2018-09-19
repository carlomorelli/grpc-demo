package com.csoft.grpcdemo.vertx;

import com.csoft.grpcdemo.licensedata.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.vertx.grpc.VertxChannelBuilder;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ClientTest {

    private ManagedChannel channel;
    private LicenseDataRequest request = LicenseDataRequest.newBuilder()
            .setInternal(Internal.newBuilder()
                    .setName("John")
                    .setSurname("Doe")
                    .build())
            .setKeyId(UUID.randomUUID().toString())
            .setOptional("Optional string")
            .setOther("Other")
            .build();

    @BeforeEach
    public void before() {
    }

    @AfterAll
    public void afterAll() {
        System.out.println("Shutting down channel...");
        channel.shutdown();
    }

    @Test
    public void testVertxServerWithTraditionalClient() throws ExecutionException, InterruptedException, TimeoutException {

        channel = ManagedChannelBuilder.forAddress("localhost", 50001)
                .usePlaintext(true)
                .build();

        LicenseDataServiceGrpc.LicenseDataServiceFutureStub asyncClient =
                LicenseDataServiceGrpc.newFutureStub(channel);

        Future<LicenseDataResponse> asyncResponse = asyncClient.fetchLicenseData(request);

        System.out.println("passed traditionalclient");
        assertEquals(MyEnum.value0, asyncResponse.get(5000, TimeUnit.MILLISECONDS).getEnum());

    }



    @Test
    public void testVertxServerWithVertxClient() {

        channel = VertxChannelBuilder.forAddress("localhost", 50001)
            .usePlaintext(true)
            .build();

        LicenseDataServiceGrpc.LicenseDataServiceVertxStub asyncClient =
            LicenseDataServiceGrpc.newVertxStub(channel);

        asyncClient.fetchLicenseData(request, asyncResult -> {

            // important: needs to be consumed
            if (asyncResult.succeeded()) {
                System.out.println("passed vertxclient");
                assertEquals(MyEnum.value0, asyncResult.result().getEnum());
            } else {
                System.out.println("Cound not reach server: " + asyncResult.cause().getMessage());
            }

        });


    }

}
