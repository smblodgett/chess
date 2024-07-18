package service;

import dataaccess.*;

public class ClearDataService {

    private final AuthDAO authData;
    private final GameDAO gameData;
    private final UserDAO userData;

    ClearDataService(AuthDAO authData, GameDAO gameData, UserDAO userData){
        this.authData=authData;
        this.gameData=gameData;
        this.userData=userData;
    }

    public void deleteAllData() {
        authData.clearAuth();
        gameData.clearGames();
        userData.clearUsers();
    }

}
