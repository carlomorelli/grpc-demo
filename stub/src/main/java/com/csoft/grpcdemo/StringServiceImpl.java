package com.csoft.grpcdemo;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.csoft.grpcdemo.stringservice.HashStringRequest;
import com.csoft.grpcdemo.stringservice.HashStringResponse;
import com.csoft.grpcdemo.stringservice.JoinStringRequest;
import com.csoft.grpcdemo.stringservice.JoinStringResponse;
import com.csoft.grpcdemo.stringservice.SplitStringRequest;
import com.csoft.grpcdemo.stringservice.SplitStringResponse;
import com.csoft.grpcdemo.stringservice.StringServiceGrpc;
import io.grpc.stub.StreamObserver;

public class StringServiceImpl extends StringServiceGrpc.StringServiceImplBase {

  @Override
  public void hashString(final HashStringRequest request, final StreamObserver<HashStringResponse> responseObserver) {

    try {
      // do something with the request
      String string = request.getInputString();
      System.out.println("Received: " + string);
      MessageDigest digest = MessageDigest.getInstance("SHA-256");
      String hash = new String(digest.digest(string.getBytes(StandardCharsets.UTF_8)));

      // append the response in the observer
      HashStringResponse response = HashStringResponse.newBuilder()
          .setHash(hash)
          .build();
      responseObserver.onNext(response);
    } catch (NoSuchAlgorithmException e) {
      responseObserver.onError(new Exception(e));
    } finally {
      responseObserver.onCompleted();
    }

  }

  @Override
  public void splitString(final SplitStringRequest request, final StreamObserver<SplitStringResponse> responseObserver) {


    // do something with the request
    String string = request.getInputString();
    System.out.println("Received: " + string);
    String[] list = string.split(" ");

    // append the response in the observer
    Stream.of(list).forEach(
        word -> {
          SplitStringResponse response = SplitStringResponse.newBuilder()
              .setWord(word).build();
          responseObserver.onNext(response);
        }

    );

    // close the observer
    responseObserver.onCompleted();

  }


  @Override
  public StreamObserver<JoinStringRequest> joinString(final StreamObserver<JoinStringResponse> responseObserver) {

    return new StreamObserver<JoinStringRequest>() {

      List<String> list = new ArrayList<>();

      @Override
      public void onNext(final JoinStringRequest request) {

        String word = request.getWord();
        System.out.println("Received word: " + word);
        list.add(word);
      }

      @Override
      public void onError(final Throwable throwable) {
      }

      @Override
      public void onCompleted() {
        String outputString = String.join(" ", list);

        responseObserver.onNext(JoinStringResponse.newBuilder().setOutputString(outputString).build());
        responseObserver.onCompleted();
      }
    };
  }
}
