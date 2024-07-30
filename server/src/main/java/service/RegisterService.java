package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import model.AuthData;
import model.UserData;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterService {

    private static DataAccessContainer data;

    RegisterService(DataAccessContainer dataAccessContainer) {
        data=dataAccessContainer;
    }

    public AuthData register(UserData userData) throws AlreadyTakenException, BadRequestException, DataAccessException {
        var username = userData.username();
        var email = userData.email();
        var password = userData.password();
        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());


        var userDataInDatabase = data.userData.getUser(username);
        if (userDataInDatabase!=null){
            throw new AlreadyTakenException("Error: already taken");
        }
        if (username==null || password==null || email==null){
            throw new BadRequestException("Error: bad request");
        }
        data.userData.createUser(username,hashedPassword,email);
        return data.authData.addAuth(username);
    }
}
