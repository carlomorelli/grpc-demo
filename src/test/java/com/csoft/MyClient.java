package com.csoft;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import com.csoft.grpcdemo.licensedata.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

public class MyClient {

    private ManagedChannel channel;
    private LicenseDataRequest request;

    @Before
    public void before() {

        request = LicenseDataRequest.newBuilder()
                .setInternal(Internal.newBuilder()
                        .setName("John")
                        .setSurname("Doe")
                        .build())
                .setKeyId(UUID.randomUUID().toString())
                .setOptional("Optional string")
                .setOther("Other")
                .build();
    }

    @After
    public void after() {
        System.out.println("Shutting down channel...");
        channel.shutdown();
    }

    @Test
    public void testMyStubServer() {

        channel = ManagedChannelBuilder.forAddress("localhost", 50000)
                .usePlaintext()
                .build();


        LicenseDataServiceGrpc.LicenseDataServiceBlockingStub syncClient =
                LicenseDataServiceGrpc.newBlockingStub(channel);

        LicenseDataResponse response = syncClient.fetchLicenseData(request);


        assertEquals(MyEnum.value0, response.getEnum());
    }
}
