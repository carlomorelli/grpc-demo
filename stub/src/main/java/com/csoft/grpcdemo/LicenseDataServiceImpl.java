package com.csoft.grpcdemo;


import com.csoft.grpcdemo.licensedata.LicenseDataRequest;
import com.csoft.grpcdemo.licensedata.LicenseDataResponse;
import com.csoft.grpcdemo.licensedata.LicenseDataServiceGrpc;
import com.csoft.grpcdemo.licensedata.MyEnum;
import io.grpc.stub.StreamObserver;

import static java.lang.String.format;

public class LicenseDataServiceImpl extends LicenseDataServiceGrpc.LicenseDataServiceImplBase {

    @Override
    public void fetchLicenseData(LicenseDataRequest request, StreamObserver<LicenseDataResponse> responseObserver) {

        LicenseDataResponse response = LicenseDataResponse.newBuilder()
                .setEnum(MyEnum.value0)
                .setSomeValue(format("This is the Stub. Answering to %s, %s, %s, %s",
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
}
