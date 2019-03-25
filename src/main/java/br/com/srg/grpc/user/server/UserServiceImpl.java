package br.com.srg.grpc.user.server;

import br.com.proto.user.*;
import br.com.srg.persistence.dao.UsersDAO;
import br.com.srg.persistence.model.Users;
import io.grpc.Status;
import io.grpc.stub.StreamObserver;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {

    @Override
    public void createUser(CreateUserRequest request, StreamObserver<CreateUserResponse> responseObserver) {

        System.out.println("Received create user request");
        User user = request.getUser();

        UsersDAO usersDAO = new UsersDAO();

        Users userPersist = new Users();

        userPersist.setUserName(user.getName());

        usersDAO.initDAOSession();
        usersDAO.saveOrUpdate(userPersist);
        usersDAO.closeSession();

        System.out.println("User successful created");

        CreateUserResponse response = CreateUserResponse.newBuilder()
                .setResult("User successful created")
                .build();

        responseObserver.onNext(response);

        responseObserver.onCompleted();

    }

    @Override
    public void readUser(ReadUserRequest request, StreamObserver<ReadUserResponse> responseObserver) {
        System.out.println("Received read user request");

        UsersDAO usersDAO = new UsersDAO();

        usersDAO.initDAOSession();

        Users resultUser =  usersDAO.getById(request.getUserId());

        usersDAO.closeSession();

        if(resultUser == null){
            System.out.println("User not found");
            responseObserver.onError(
                    Status.NOT_FOUND
                    .withDescription("User not found")
                    .asRuntimeException()
            );
        } else {
            System.out.println("User found, sending response");
            responseObserver.onNext(
                    ReadUserResponse.newBuilder()
                            .setUser(userDBToUser(resultUser))
                            .build());

            responseObserver.onCompleted();

        }

    }

    private User userDBToUser(Users user){
        return User.newBuilder()
                .setId(user.getUserId())
                .setName(user.getUserName())
                .build();
    }
}
