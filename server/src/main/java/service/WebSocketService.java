package service;

import dataaccess.DataAccessContainer;

public class WebSocketService {

    private static DataAccessContainer data;

    WebSocketService(DataAccessContainer dataAccessContainer) {
        data=dataAccessContainer;
    }


}
