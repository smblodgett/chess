package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import model.GameData;

public class CreateGameService {

    private DataAccessContainer data;

    public CreateGameService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

    public void createNewGame(GameData game){
        data.gameData.createGame(game);
    }

    public boolean checkUniqueID(int gameID){
        try {
            GameData thisGame = data.gameData.getGame(gameID);
            return false;
        }
        catch (DataAccessException e) {
            return true;
        }
    }


}
