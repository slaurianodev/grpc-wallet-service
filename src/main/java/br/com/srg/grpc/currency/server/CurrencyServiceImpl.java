package br.com.srg.grpc.currency.server;

import br.com.proto.currency.*;
import br.com.srg.persistence.dao.CurrenciesDAO;
import br.com.srg.persistence.model.Currencies;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class CurrencyServiceImpl extends CurrencyServiceGrpc.CurrencyServiceImplBase {

    @Override
    public void createCurrency(CreateCurrencyRequest request, StreamObserver<CreateCurrencyResponse> responseObserver) {
        System.out.println("Received create user request");

        try{
            Currency currency = request.getCurrency();

            CurrenciesDAO currenciesDAO = new CurrenciesDAO();

            Currencies currenciesPersist = new Currencies();

            currenciesPersist.setCurrencyName(currency.getCurrencyName());
            currenciesPersist.setCurrencyCode(currency.getCurrencyCode());

            currenciesDAO.initDAOSession();
            currenciesDAO.saveOrUpdate(currenciesPersist);
            currenciesDAO.closeSession();

            System.out.println("Currency successful created");

            CreateCurrencyResponse response = CreateCurrencyResponse.newBuilder()
                    .setResult("Currency successful created")
                    .build();

            responseObserver.onNext(response);

            responseObserver.onCompleted();
        } catch (Exception e){
            responseObserver.onError(
                    Status.INTERNAL
                        .withDescription("There is an error during creating currency process.")
                        .augmentDescription(e.getLocalizedMessage())
                        .asRuntimeException()
            );
        }

    }

    @Override
    public void listCurrencies(ListCurrenciesRequest request, StreamObserver<ListCurrenciesResponse> responseObserver) {
        CurrenciesDAO currenciesDAO = new CurrenciesDAO();

        currenciesDAO.initDAOSession();
        currenciesDAO.listAll().iterator().forEachRemaining(
                currency -> responseObserver.onNext(
                        ListCurrenciesResponse.newBuilder().setCurrency(persistToObject(currency)).build()
                )
        );
        currenciesDAO.closeSession();

        responseObserver.onCompleted();
    }

    private Currency persistToObject(Currencies currencies){
        return Currency.newBuilder()
                .setCurrencyId(currencies.getCurrencyId())
                .setCurrencyCode(currencies.getCurrencyCode())
                .setCurrencyName(currencies.getCurrencyName())
                .build();
    }
}
