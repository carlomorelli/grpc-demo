package com.csoft.grpcdemo.vertx;


import com.csoft.grpcdemo.licensedata.LicenseDataRequest;
import com.csoft.grpcdemo.licensedata.LicenseDataResponse;
import com.csoft.grpcdemo.licensedata.LicenseDataServiceGrpc;
import com.csoft.grpcdemo.licensedata.MyEnum;
import io.grpc.stub.StreamObserver;
import io.vertx.core.Future;

import static java.lang.String.format;

public class LicenseDataServiceImpl extends LicenseDataServiceGrpc.LicenseDataServiceVertxImplBase {


    @Override
    public void fetchLicenseData(final LicenseDataRequest request, final Future<LicenseDataResponse> futureResponse) {

        LicenseDataResponse response = LicenseDataResponse.newBuilder()
                .setEnum(MyEnum.value0)
                .setSomeValue(format("This is Vert.x. Answering to %s, %s, %s, %s",
                        request.getKeyId(),
                        request.getOptional(),
                        request.getOther(),
                        request.getInternal().getName() + request.getInternal().getSurname()))
                .build();

        System.out.println("Getting request: " + request);
        System.out.println("Answering: " + response);
        futureResponse.complete(response);

    }
}


/*
// Alternative, using the traditional way:
public class LicenseDataServiceImpl extends LicenseDataServiceGrpc.LicenseDataServiceImplBase {

    @Override
    public void fetchLicenseData(LicenseDataRequest request, StreamObserver<LicenseDataResponse> responseObserver) {

        LicenseDataResponse response = LicenseDataResponse.newBuilder()
            .setEnum(MyEnum.value0)
            .setSomeValue(format("This is Vert.x. Answering to %s, %s, %s, %s",
                request.getKeyId(),
                request.getOptional(),
                request.getOther(),
                request.getInternal().getName() + request.getInternal().getSurname()))
            .build();

        System.out.println("Getting request: " + request);
        System.out.println("Answering: " + response);

        responseObserver.onNext(response);
        responseObserver.onCompleted();

    }

}*/
