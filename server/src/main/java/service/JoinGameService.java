package service;

import chess.ChessGame;
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

    public GameData findGame(int gameID) throws BadRequestException {
        try {
            return data.gameData.getGame(gameID);
        }
        catch (DataAccessException noSuchGame){
            throw new BadRequestException("Error: bad request");
        }
    }

    public GameData addPlayer(GameData gameData, String color, String username) throws BadRequestException, DataAccessException {

        if (color.equals("WHITE")) {
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.WHITE;
            data.gameData.updateGamePlayer(gameData,teamColor,username);
            return new GameData(gameData.gameID(),username, gameData.blackUsername(), gameData.gameName(), gameData.game());
        }
        else if (color.equals("BLACK")){
            ChessGame.TeamColor teamColor = ChessGame.TeamColor.BLACK;
            data.gameData.updateGamePlayer(gameData,teamColor,username);
            return new GameData(gameData.gameID(),gameData.whiteUsername(),username, gameData.gameName(), gameData.game());
        }
        else throw new BadRequestException("Error: bad request");
    }
}
