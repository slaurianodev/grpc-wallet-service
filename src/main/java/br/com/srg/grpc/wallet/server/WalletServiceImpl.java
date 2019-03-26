package br.com.srg.grpc.wallet.server;

import br.com.proto.wallet.*;
import br.com.srg.persistence.dao.CurrenciesDAO;
import br.com.srg.persistence.dao.UsersDAO;
import br.com.srg.persistence.dao.WalletsDAO;
import br.com.srg.persistence.model.Currencies;
import br.com.srg.persistence.model.Users;
import br.com.srg.persistence.model.Wallets;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

import java.sql.SQLException;
import java.util.HashMap;

public class WalletServiceImpl extends WalletServiceGrpc.WalletServiceImplBase {

    @Override
    public void deposit(DepositRequest request, StreamObserver<DepositResponse> responseObserver) {
        System.out.println("Received deposit request");

        try{
            Wallet wallet = request.getWallet();

            WalletsDAO walletsDAO = new WalletsDAO();

            Users userPersist = getUserPersist(wallet.getUserId());

            if(userPersist == null){
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("There is an error during deposit process.")
                                .withDescription("User not found")
                                .asRuntimeException()
                );
            }

            Currencies currenciyPersit = getCurrency(wallet.getCurrencyCode());

            if(currenciyPersit == null){
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("There is an error during deposit process.")
                                .withDescription("Currency not found")
                                .asRuntimeException()
                );
            }

            Wallets walletPersist = getWallet(userPersist,currenciyPersit);

            if(walletPersist == null){
                Wallets newWallet = new Wallets();
                newWallet.setUser(userPersist);
                newWallet.setCurrency(currenciyPersit);
                newWallet.setAmount(wallet.getAmount());
                walletsDAO.initDAOSession();
                walletsDAO.saveOrUpdate(newWallet);
                walletsDAO.closeSession();
            } else {
                walletPersist.setAmount(walletPersist.getAmount() + wallet.getAmount());
                walletsDAO.initDAOSession();
                walletsDAO.saveOrUpdate(walletPersist);
                walletsDAO.closeSession();
            }

            responseObserver.onNext(
                    DepositResponse.newBuilder()
                            .build()
            );

            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("There is an error during deposit process.")
                            .augmentDescription(e.getLocalizedMessage())
                            .asRuntimeException()
            );
        }
    }

    @Override
    public void withdraw(WithdrawRequest request, StreamObserver<WithdrawResponse> responseObserver) {
        System.out.println("Received withdraw request");

        try{
            Wallet wallet = request.getWallet();

            WalletsDAO walletsDAO = new WalletsDAO();

            Users userPersist = getUserPersist(wallet.getUserId());

            if(userPersist == null){
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("There is an error during deposit process.")
                                .withDescription("User not found")
                                .asRuntimeException()
                );
            }

            Currencies currenciyPersit = getCurrency(wallet.getCurrencyCode());

            if(currenciyPersit == null){
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("There is an error during deposit process.")
                                .withDescription("Currency not found")
                                .asRuntimeException()
                );
            }

            Wallets walletPersist = getWallet(userPersist,currenciyPersit);

            if(walletPersist == null){
                responseObserver.onError(
                        Status.NOT_FOUND
                                .withDescription("There is an error during deposit process.")
                                .withDescription("Wallet not found")
                                .asRuntimeException()
                );
            } else {
                if(walletPersist.getAmount() < wallet.getAmount()){
                    responseObserver.onError(
                            Status.INVALID_ARGUMENT
                                    .withDescription("There is an error during withdraw process.")
                                    .withDescription("Insufficient funds")
                                    .asRuntimeException()
                    );
                } else {
                    walletPersist.setAmount(walletPersist.getAmount() - wallet.getAmount());
                    walletsDAO.initDAOSession();
                    walletsDAO.saveOrUpdate(walletPersist);
                    walletsDAO.closeSession();
                }

            }

            responseObserver.onNext(
                    WithdrawResponse.newBuilder().build()
            );

            responseObserver.onCompleted();

        }catch (Exception e){
            responseObserver.onError(
                    Status.INTERNAL
                            .withDescription("There is an error during withdraw process.")
                            .augmentDescription(e.getLocalizedMessage())
                            .asRuntimeException()
            );
        }
    }

    private Users getUserPersist(Integer userId) throws SQLException{
        UsersDAO usersDAO = new UsersDAO();

        usersDAO.initDAOSession();
        Users user = usersDAO.getById(userId);
        usersDAO.closeSession();
        return user;
    }

    private Currencies getCurrency(String currencyCode) throws SQLException{
        CurrenciesDAO currenciesDAO = new CurrenciesDAO();

        currenciesDAO.initDAOSession();
        Currencies currency = currenciesDAO.getByCurrencyCode(currencyCode);
        currenciesDAO.closeSession();
        return currency;
    }

    private Wallets getWallet(Users user, Currencies currency) throws SQLException {
        WalletsDAO walletsDAO = new WalletsDAO();

        HashMap<String,Object> params = new HashMap<String, Object>();
        params.put("userId",user.getUserId());
        params.put("currencyId", currency.getCurrencyId());

        walletsDAO.initDAOSession();
        Wallets wallet = walletsDAO.getByUserAndCurrency(params);

        return wallet;
    }
}
