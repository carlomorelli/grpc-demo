package com.csoft.grpcdemo.spring;

import com.csoft.grpcdemo.licensedata.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

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
    public void testSpringBootGrpcServer() {
        channel = ManagedChannelBuilder.forAddress("localhost", 50002)
                .usePlaintext()
                .build();
        LicenseDataServiceGrpc.LicenseDataServiceBlockingStub syncClient =
                LicenseDataServiceGrpc.newBlockingStub(channel);
        LicenseDataResponse response = syncClient.fetchLicenseData(request);
        assertEquals(MyEnum.value0, response.getEnum());
    }

}
