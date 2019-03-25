package br.com.srg.grpc.user.client;

import br.com.proto.user.CreateUserRequest;
import br.com.proto.user.CreateUserResponse;
import br.com.proto.user.User;
import br.com.proto.user.UserServiceGrpc;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

public class UserClient {

    public static void main(String[] args) {
        System.out.println("Start user client");

        UserClient main = new UserClient();

        main.run();
    }

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9001)
                .usePlaintext()
                .build();

        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(channel);

        User user = User.newBuilder()
                    .setName("Lauriano")
                    .build();

        CreateUserResponse createUserResponse = userClient.createUser(
                CreateUserRequest.newBuilder()
                        .setUser(user)
                        .build()
        );

        System.out.println("Received create user response");
        System.out.println(createUserResponse.getResult());

    }
}
