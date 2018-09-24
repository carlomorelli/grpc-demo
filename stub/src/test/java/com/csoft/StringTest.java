package com.csoft;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import com.csoft.grpcdemo.stringservice.HashStringRequest;
import com.csoft.grpcdemo.stringservice.HashStringResponse;
import com.csoft.grpcdemo.stringservice.JoinStringRequest;
import com.csoft.grpcdemo.stringservice.JoinStringResponse;
import com.csoft.grpcdemo.stringservice.SplitStringRequest;
import com.csoft.grpcdemo.stringservice.SplitStringResponse;
import com.csoft.grpcdemo.stringservice.StringServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.junit.Test;

public class StringTest {

  private ManagedChannel channel;


  @Test
  public void testUnary() {
    System.out.println("Starting channel...");
    channel = ManagedChannelBuilder.forAddress("localhost", 50000)
        .usePlaintext()
        .build();

    // test code here

    StringServiceGrpc.StringServiceBlockingStub client = StringServiceGrpc.newBlockingStub(channel);

    HashStringResponse response = client.hashString(HashStringRequest.newBuilder().setInputString("something").build());

    System.out.println("Received hash in response: " + response.getHash());

    System.out.println("Shutting down channel...");
    channel.shutdown();
  }

  @Test
  public void testServerStream() {
    System.out.println("Starting channel...");
    channel = ManagedChannelBuilder.forAddress("localhost", 50000)
        .usePlaintext()
        .build();


    Dictionary<String, String> x = new Hashtable<>();

    // test code here

    StringServiceGrpc.StringServiceBlockingStub client = StringServiceGrpc.newBlockingStub(channel);

    SplitStringRequest request = SplitStringRequest.newBuilder()
        .setInputString("call me ishmael")
        .build();

    client.splitString(request).forEachRemaining(
        response -> System.out.println("Received word: " + response.getWord())
    );

    System.out.println("Shutting down channel...");
    channel.shutdown();
  }


  @Test
  public void testClientStream() {
    System.out.println("Starting channel...");
    channel = ManagedChannelBuilder.forAddress("localhost", 50000)
        .usePlaintext()
        .build();

    // test code here

    StringServiceGrpc.StringServiceStub client = StringServiceGrpc.newStub(channel);
    CountDownLatch latch = new CountDownLatch(1);


    StreamObserver<JoinStringResponse> responseStreamObserver = new StreamObserver<JoinStringResponse>() {
      @Override
      public void onNext(final JoinStringResponse joinStringResponse) {
        String outputString = joinStringResponse.getOutputString();
        System.out.println("Received response from joinString call: " + outputString);
      }

      @Override
      public void onError(final Throwable throwable) {
      }

      @Override
      public void onCompleted() {
        latch.countDown();
      }
    };

    StreamObserver<JoinStringRequest> requestObserver = client.joinString(responseStreamObserver);

    System.out.println("Sending 'call'");
    requestObserver.onNext(JoinStringRequest.newBuilder().setWord("call").build());
    System.out.println("Sending 'me'");
    requestObserver.onNext(JoinStringRequest.newBuilder().setWord("me").build());
    System.out.println("Sending 'ishmael'");
    requestObserver.onNext(JoinStringRequest.newBuilder().setWord("ishmael").build());
    requestObserver.onCompleted();

    try {
      latch.await(3000, TimeUnit.MILLISECONDS);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }


    System.out.println("Shutting down channel...");
    channel.shutdown();

  }


}
