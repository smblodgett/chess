package service;

import dataaccess.DataAccessContainer;

public class ListGameService {

    private DataAccessContainer data;

    ListGameService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

}
