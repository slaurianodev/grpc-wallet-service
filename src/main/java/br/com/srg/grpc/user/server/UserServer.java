package br.com.srg.grpc.user.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class UserServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Init Wallet Server");

        Server server = ServerBuilder.forPort(9001)
                .addService(new UserServiceImpl())
                .addService(ProtoReflectionService.newInstance()) //reflection
                .build();

        server.start();

        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Received Shutdown Request");
            server.shutdown();
            System.out.println("Done");
        }));

        server.awaitTermination();
    }
}
