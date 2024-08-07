package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Objects;

public class GameMemoryDAO implements GameDAO {
    static ArrayList<GameData> gameDatabase  = new ArrayList<>();

    public GameMemoryDAO(){
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
    public ArrayList<GameData> listGames() {
        return gameDatabase;
    }

    @Override
    public void updateGamePlayer(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {

        int gameID = gameData.gameID();
        ChessGame myGame = gameData.game();
        String gameName = gameData.gameName();
        String blackName = gameData.blackUsername();
        String whiteName = gameData.whiteUsername();
        boolean isOver = gameData.isOver();

        if (color == ChessGame.TeamColor.WHITE){
            if (whiteName!=null) {throw new DataAccessException("someone else is already white!");}
            GameData updatedGame = new GameData(gameID,username,blackName,gameName,myGame,isOver);
            deleteGame(gameID);
            createGame(updatedGame);
        }
        if (color == ChessGame.TeamColor.BLACK){
            if (blackName!=null) {throw new DataAccessException("someone else is already black!");}
            GameData updatedGame = new GameData(gameID,whiteName,username,gameName,myGame,isOver);
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

    @Override
    public void updateGame(int gameID, ChessGame newGame) throws DataAccessException {

    }

    @Override
    public void removePlayer(int gameID, String username,String colorSwitch) throws DataAccessException {

    }

    @Override
    public void isOver(int gameID) throws DataAccessException {
    }
}
