package service;

import dataaccess.DataAccessContainer;

public class LogoutService {

    private DataAccessContainer data;

    LogoutService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }
}
