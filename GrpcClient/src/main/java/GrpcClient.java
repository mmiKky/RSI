import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;
import org.example.model.*;

import java.util.Iterator;

public class GrpcClient {

    public static void main(String[] args) {
        String address = "localhost";
        int port = 50001;
        ExampleServiceGrpc.ExampleServiceBlockingStub bStub;
        ExampleServiceGrpc.ExampleServiceStub nonbStub;

        System.out.println("Running gRPC client...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
        bStub = ExampleServiceGrpc.newBlockingStub(channel);
        nonbStub = ExampleServiceGrpc.newStub(channel);

        TheRequest request  = TheRequest.newBuilder().setName("Antek").setAge(20).build();

        System.out.println("...calling unaryProcedure");
        TheResponse response = bStub.unaryProcedure(request);
        System.out.println("...after calling unaryProcedure");
        System.out.println("Response: " + response);

        Iterator<TheResponse> respIterator;
        System.out.println("...calling streamProcedure");
        respIterator = bStub.streamProcedure(request);
        System.out.println("...after calling streamProcedure");
        TheResponse strResponse;
        while(respIterator.hasNext()) {
            strResponse = respIterator.next();
            System.out.println("streamProcedure response: " + strResponse.getMessage());
        }

        nonbStub = ExampleServiceGrpc.newStub(channel);
        System.out.println("...async calling unaryProcedure");
        nonbStub.unaryProcedure(request, new UnaryObs());
        System.out.println("...after async calling unaryProcedure");

        System.out.println("...async calling streamProcedure");
        nonbStub.streamProcedure(request, new UnaryObs());
        System.out.println("...after async calling streamProcedure");

        OperationReq operationReq = OperationReq.newBuilder().setIsFib(false).setElementsNr(10).build();
        nonbStub.operationProcedure(operationReq, new OperationObserver());

        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        channel.shutdown();
    }

    public static class UnaryObs implements StreamObserver<TheResponse> {
        @Override
        public void onNext(TheResponse theResponse) {
            System.out.println("async unary onNext: " + theResponse.getMessage());
        }

        @Override
        public void onError(Throwable throwable) {
            System.out.println("async unary onError");
        }

        @Override
        public void onCompleted() {
            System.out.println("async unary onCompleted");
        }
    }

    public static class OperationObserver implements StreamObserver<OperationRes> {

        @Override
        public void onNext(OperationRes operationRes) {
            System.out.println("async stream onNext: " + operationRes.getResult() + ", " + operationRes.getResultSqr());
        }

        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onCompleted() {

        }
    }
}
