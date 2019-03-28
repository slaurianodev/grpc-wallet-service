package br.com.srg.grpc.user.client;

import br.com.proto.user.*;
import io.grpc.Deadline;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.stub.StreamObserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

public class UserClient {

    public static void main(String[] args) throws IOException {

        System.out.println("Start user client");
        System.out.println("Please, choose one operation:");
        System.out.println("0 - create user\n1 - get user\n");

        UserClient main = new UserClient();

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int operation = Integer.parseInt(br.readLine().trim());

        main.run(operation);

        br.close();
    }

    private void run(int operation) throws IOException {

        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9001)
                .usePlaintext()
                .build();

        switch (operation){
            case 0:
                createUser(channel);
                break;
            case 1:
                getUser(channel);
                break;

            default:
                System.out.println("Operation invalid");
        }
    }

    private void createUser(ManagedChannel channel) throws IOException {

        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(channel);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Inform currency name: ");
        String userName = br.readLine().trim();

        User user = User.newBuilder()
                    .setName(userName)
                    .build();

        CreateUserResponse createUserResponse = userClient.createUser(
                CreateUserRequest.newBuilder()
                        .setUser(user)
                        .build()
        );

        System.out.println("Received create user response");
        System.out.println(createUserResponse.getResult());

    }

    private void getUser(ManagedChannel channel) throws IOException {
        UserServiceGrpc.UserServiceBlockingStub userClient = UserServiceGrpc.newBlockingStub(channel);

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Inform user Id: ");
        int userId = Integer.parseInt(br.readLine().trim());

        ReadUserResponse readUserResponse = userClient.readUser(ReadUserRequest.newBuilder()
                        .setUserId(userId)
                        .build()
        );

        System.out.println("Received read user response");
        System.out.println(readUserResponse.getUser());
    }
}
