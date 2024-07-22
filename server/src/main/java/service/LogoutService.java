package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.UnauthorizedException;

public class LogoutService {

    private DataAccessContainer data;

    LogoutService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

    public void logout(String authToken) throws UnauthorizedException, DataAccessException {
        if (data.authData.getAuth(authToken)!=null){
            data.authData.clearAuth(authToken); // might need to just clear one user's data
        }
        else {
            throw new UnauthorizedException("Error: unauthorized");
        }
    }
}
