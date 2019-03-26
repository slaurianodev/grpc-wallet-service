package br.com.srg.grpc.currency.client;

import br.com.proto.currency.*;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;

public class CurrencyClient {

    public static void main(String[] args) {
        System.out.println("Start currency client");

        CurrencyClient main = new CurrencyClient();

        main.run();
    }

    private void run(){
        ManagedChannel channel = ManagedChannelBuilder.forAddress("localhost",9002)
                .usePlaintext()
                .build();

//        createCurrency(channel);
        listAllCurrencies(channel);
    }

    private void createCurrency(ManagedChannel channel){
        CurrencyServiceGrpc.CurrencyServiceBlockingStub currencyClient = CurrencyServiceGrpc.newBlockingStub(channel);

        Currency currency = Currency.newBuilder()
//                        .setCurrencyName("Bla")
//                        .setCurrencyCode(null)
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
