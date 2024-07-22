package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import java.util.HashSet;

public class ListGameService {

    private DataAccessContainer data;

    ListGameService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

    public HashSet<GameData> list(String authToken) throws DataAccessException, UnauthorizedException {
        if (data.authData.getAuth(authToken)==null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return data.gameData.listGames();
    }

}
