package br.com.srg.grpc.currency.server;

import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.protobuf.services.ProtoReflectionService;

import java.io.IOException;

public class CurrencyServer {

    public static void main(String[] args) throws IOException, InterruptedException {
        System.out.println("Init Currency Server");

        Server server = ServerBuilder.forPort(9002)
                .addService(new CurrencyServiceImpl())
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
