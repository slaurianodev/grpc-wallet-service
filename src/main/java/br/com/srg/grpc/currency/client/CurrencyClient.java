package br.com.srg.grpc.currency.client;

import br.com.proto.currency.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class CurrencyClient {

    public static void main(String[] args) throws IOException {
        System.out.println("Start currency client");
        System.out.println("Please, choose one operation:");
        System.out.println("0 - create a new currency\n1 - list a existent currencies\n");

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        int operation = Integer.parseInt(br.readLine().trim());

        CurrencyClient main = new CurrencyClient();

        main.run(operation);

        br.close();
    }

    private void run(int operation) throws IOException {
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9002)
                .usePlaintext()
                .build();

        switch (operation){
            case 0:
                createCurrency(channel);
                break;
            case 1:
                listAllCurrencies(channel);
                break;

            default:
                System.out.println("Operation invalid");
        }

    }

    private void createCurrency(ManagedChannel channel) throws IOException {

        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("Inform currency name: ");
        String currencyName = br.readLine().trim();

        System.out.println("Inform currency code: ");
        String currencyCode = br.readLine().trim().toUpperCase();


        CurrencyServiceGrpc.CurrencyServiceBlockingStub currencyClient = CurrencyServiceGrpc.newBlockingStub(channel);
        Currency currency = Currency.newBuilder()
                        .setCurrencyName(currencyName)
                        .setCurrencyCode(currencyCode)
                .build();

        CreateCurrencyResponse response = currencyClient.createCurrency(
                CreateCurrencyRequest.newBuilder()
                        .setCurrency(currency)
                        .build()
        );

        System.out.println("Received create currency response");
        System.out.println(response.getResult());
    }

    private void listAllCurrencies(ManagedChannel channel){
        CurrencyServiceGrpc.CurrencyServiceBlockingStub currencyClient = CurrencyServiceGrpc.newBlockingStub(channel);

        currencyClient.listCurrencies(ListCurrenciesRequest.newBuilder().build()).forEachRemaining(
                currencies -> System.out.println(currencies.getCurrency().toString())
        );

    }
}
