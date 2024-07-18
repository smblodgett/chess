package service;

import dataaccess.*;

public class ClearDataService {

    public void deleteAllData() {
        AuthDAO.clearAuth();
        GameDAO.clearGames();
        UserDAO.clearUsers();
    }

}
