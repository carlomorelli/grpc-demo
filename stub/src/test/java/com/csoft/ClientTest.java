package com.csoft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.csoft.grpcdemo.licensedata.*;
import com.google.common.util.concurrent.ListenableFuture;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

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

    @Before
    public void before() {
    }

    @After
    public void after() {
        System.out.println("Shutting down channel...");
        channel.shutdown();
    }

    @Test
    public void testStubGrpcServer() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50000)
                .usePlaintext()
                .build();
        LicenseDataServiceGrpc.LicenseDataServiceBlockingStub syncClient =
                LicenseDataServiceGrpc.newBlockingStub(channel);
        LicenseDataResponse response = syncClient.fetchLicenseData(request);
        assertEquals(MyEnum.value0, response.getEnum());
    }

    @Test
    public void testSpringBootGrpcServer() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50002)
                .usePlaintext()
                .build();
        LicenseDataServiceGrpc.LicenseDataServiceBlockingStub syncClient =
                LicenseDataServiceGrpc.newBlockingStub(channel);
        LicenseDataResponse response = syncClient.fetchLicenseData(request);
        assertEquals(MyEnum.value0, response.getEnum());
    }




    @Test
    public void testVertxServer() {

        channel = ManagedChannelBuilder.forAddress("localhost", 50001)
                .usePlaintext()
                .build();

        LicenseDataServiceGrpc.LicenseDataServiceFutureStub asyncClient =
                LicenseDataServiceGrpc.newFutureStub(channel);

/*
        asyncClient.fetchLicenseData(request, asyncResponse -> {
            asyncResponse.su


                }


                );


        assertEquals(MyEnum.value0, response.get().getEnum());
*/

    }
}
