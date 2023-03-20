import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.example.model.*;

import java.io.IOException;

public class GrpcServer {

    public static void main(String[] args) {
        int port = 50001;

        System.out.println("Starting gRPC server...");
        Server server = ServerBuilder.forPort(port).addService(new ExampleServiceImpl()).build();

        try {
            server.start();
            System.out.println("...Server started");
            server.awaitTermination();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }

    static class ExampleServiceImpl extends ExampleServiceGrpc.ExampleServiceImplBase{

        public void unaryProcedure(TheRequest req, StreamObserver<TheResponse> responseObserver) {
            String msg;
            System.out.println("...called UnaryProcedure - start");
            if (req.getAge() > 18)
                msg = "Mr/Ms " + req.getName();
            else
                msg = "Boy/Girl";
            TheResponse response = TheResponse.newBuilder().setMessage("Hello " + msg).build();

            try{
                Thread.sleep(600L);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("...called UnaryProcedure - end");
        }

        @Override
        public void streamProcedure(TheRequest request, StreamObserver<TheResponse> responseObserver) {
            int chunksNr = 6;

            System.out.println("...called StreamProcedure - start");
            for(int i=0; i < chunksNr; ++i){
                TheResponse response = TheResponse.newBuilder().setMessage("Stream chunk " + (i+1)).build();

                try {
                    Thread.sleep(300L);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                responseObserver.onNext(response);
            }
            responseObserver.onCompleted();

            System.out.println("...called StreamProcedure - end");
        }

        @Override
        public void operationProcedure(OperationReq request, StreamObserver<OperationRes> responseObserver) {

            if (request.getIsFib()) {
                int prev = 0, current = 1;

                for (int i = 0; i < request.getElementsNr(); ++i) {
                    int buff = current;
                    current += prev;
                    prev = buff;

                    OperationRes resp = OperationRes.newBuilder().setResult(current).setResultSqr(current * current).build();
                    responseObserver.onNext(resp);
                }
            }
            else {
                for(int i = 1, result = 1; i <= request.getElementsNr(); ++i){
                    result *= i;

                    OperationRes resp = OperationRes.newBuilder().setResult(result).setResultSqr(result * result).build();
                    responseObserver.onNext(resp);
                }
            }
            responseObserver.onCompleted();
        }
    }
}
