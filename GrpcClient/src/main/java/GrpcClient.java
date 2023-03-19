import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import org.example.model.ExampleServiceGrpc;
import org.example.model.TheRequest;
import org.example.model.TheResponse;

public class GrpcClient {

    public static void main(String[] args) {
        String address = "localhost";
        int port = 50001;

        ExampleServiceGrpc.ExampleServiceBlockingStub bStub;
        System.out.println("Running gRPC client...");
        ManagedChannel channel = ManagedChannelBuilder.forAddress(address, port).usePlaintext().build();
        bStub = ExampleServiceGrpc.newBlockingStub(channel);

        TheRequest request  = TheRequest.newBuilder().setName("Antek").setAge(20).build();

        System.out.println("...calling unaryProcedure");
        TheResponse response = bStub.unaryProcedure(request);
        System.out.println("...after calling unaryProcedure");
        System.out.println("Response: " + response);

        channel.shutdown();
    }
}
