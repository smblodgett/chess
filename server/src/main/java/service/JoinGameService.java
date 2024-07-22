package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;

public class JoinGameService {

    private DataAccessContainer data;

    JoinGameService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

    public AuthData authenticate(String authToken) throws UnauthorizedException {
        if (data.authData.getAuth(authToken)==null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return data.authData.getAuth(authToken);
    }

    public GameData findGame(int gameID) throws DataAccessException {
        return data.gameData.getGame(gameID);
    }

    public GameData addPlayer(GameData gameData, String color, String username) throws BadRequestException {
        if (color.equals("WHITE")) {
            return new GameData(gameData.gameID(),username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else if (color.equals("BLACK")){
            return new GameData(gameData.gameID(),gameData.whiteUsername(),username, gameData.gameName(), gameData.game());
        }
        else throw new BadRequestException("Error: bad request");
    }
}
