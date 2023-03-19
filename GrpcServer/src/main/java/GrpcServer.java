import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import org.example.model.ExampleServiceGrpc;
import org.example.model.TheRequest;
import org.example.model.TheResponse;

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
            System.out.println("...called UnaryProcedure");
            if (req.getAge() > 18)
                msg = "Mr/Ms " + req.getName();
            else
                msg = "Boy/Girl";
            TheResponse response = TheResponse.newBuilder().setMessage("Hello " + msg).build();

            try{
                Thread.sleep(2000);
            } catch (InterruptedException e){
                e.printStackTrace();
            }

            responseObserver.onNext(response);
            responseObserver.onCompleted();
            System.out.println("...called UnaryProcedure - end");
        }
    }
}
