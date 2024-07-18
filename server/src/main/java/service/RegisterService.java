package service;

import dataaccess.DataAccessContainer;

public class RegisterService {

    private DataAccessContainer data;

    RegisterService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }
}
