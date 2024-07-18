package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.HashSet;
import java.util.Objects;

public class GameDataAccess implements GameDAO {
    HashSet<GameData> gameDatabase;

    public GameDataAccess(){
        gameDatabase = new HashSet<>();
    }

    @Override
    public GameData createGame(GameData gameData) {
        gameDatabase.add(gameData);
        return gameData;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException{
        for (var game : gameDatabase) { // find game in database, if not throw exception
            if (Objects.equals(game.gameID(), gameID)){
                return game;
            }
        }
        throw new DataAccessException("dude! this game isn't in the database!");
    }

    @Override
    public HashSet<GameData> listGames() {
        return gameDatabase;
    }

    @Override
    public void updateGamePlayer(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {

        int gameID = gameData.gameID();
        ChessGame myGame = gameData.game();
        String gameName = gameData.gameName();
        String blackName = gameData.blackUsername();
        String whiteName = gameData.whiteUsername();

        if (color == ChessGame.TeamColor.WHITE){
            if (whiteName!=null) throw new DataAccessException("someone else is already white!");
            GameData updatedGame = new GameData(gameID,username,blackName,gameName,myGame);
            deleteGame(gameID);
            createGame(updatedGame);
        }
        if (color == ChessGame.TeamColor.BLACK){
            if (blackName!=null) throw new DataAccessException("someone else is already black!");
            GameData updatedGame = new GameData(gameID,whiteName,username,gameName,myGame);
            deleteGame(gameID);
            createGame(updatedGame);
        }
    }

    @Override
    public void clearGames() {
            gameDatabase.clear();
    }

    @Override
    public void deleteGame(int gameID){
        gameDatabase.removeIf(game -> game.gameID() == gameID);
    }
}
