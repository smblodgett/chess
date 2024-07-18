package service;

import dataaccess.DataAccessContainer;

public class JoinGameService {

    private DataAccessContainer data;

    JoinGameService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }
}
