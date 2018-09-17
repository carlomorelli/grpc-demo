package com.csoft.grpcdemo.vertx;

import com.csoft.grpcdemo.licensedata.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.UUID;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertEquals;


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

    @AfterEach
    public void after() {
        System.out.println("Shutting down channel...");
        channel.shutdown();
    }

    @Test
    public void testVertxServer() throws ExecutionException, InterruptedException, TimeoutException {

        channel = ManagedChannelBuilder.forAddress("localhost", 50001)
                .usePlaintext(true)
                .build();

        LicenseDataServiceGrpc.LicenseDataServiceFutureStub asyncClient =
                LicenseDataServiceGrpc.newFutureStub(channel);

        Future<LicenseDataResponse> asyncResponse = asyncClient.fetchLicenseData(request);

        assertEquals(MyEnum.value0, asyncResponse.get(2000, TimeUnit.MILLISECONDS).getEnum());

    }
}
