package service;

import dataaccess.*;

public class ClearDataService {

    private final AuthDAO authData;
    private final GameDAO gameData;
    private final UserDAO userData;

    ClearDataService(DataAccessContainer dataAccessContainer){
        this.authData=dataAccessContainer.authData;
        this.gameData=dataAccessContainer.gameData;
        this.userData=dataAccessContainer.userData;
    }

    public void clearAllData() throws DataAccessException {
        authData.clearAllAuth();
        gameData.clearGames();
        userData.clearUsers();
    }

}
