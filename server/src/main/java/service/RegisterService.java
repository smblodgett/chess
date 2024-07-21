package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.AuthData;
import model.UserData;

public class RegisterService {

    private DataAccessContainer data;

    RegisterService(DataAccessContainer dataAccessContainer) throws AlreadyTakenException, BadRequestException, DataAccessException {
        this.data=dataAccessContainer;
    }

    public AuthData register(UserData userData) throws AlreadyTakenException, BadRequestException {
        var username = userData.username();
        var email = userData.email();
        var password = userData.password();

        var userDataInDatabase = data.userData.getUser(username);
        if (userDataInDatabase!=null){
            throw new AlreadyTakenException("Error: already taken");
        }
        if (username==null || password==null || email==null){
            throw new BadRequestException("Error: bad request");
        }
        data.userData.createUser(username,password,email);
        return data.authData.addAuth(username);
    }
}
