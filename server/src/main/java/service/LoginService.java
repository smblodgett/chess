package service;

import dataaccess.DataAccessContainer;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import org.mindrot.jbcrypt.BCrypt;

import java.util.Objects;

public class LoginService {

    private DataAccessContainer data;

    LoginService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

    public AuthData login(String username, String password) throws Exception {
        if (data.userData.getUser(username)!=null) {
            if (BCrypt.checkpw(password, data.userData.getUser(username).password())){
                return data.authData.addAuth(username);
            }
            throw new UnauthorizedException("Error: unauthorized");
        }
        throw new UnauthorizedException("Error: unauthorized");
    }
}
