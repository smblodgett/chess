package service;

import dataaccess.DataAccessContainer;

public class LoginService {

    private DataAccessContainer data;

    LoginService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }
}
