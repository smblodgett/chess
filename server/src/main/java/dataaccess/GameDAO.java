package dataaccess;

import chess.ChessGame;
import exception.BadRequestException;
import model.GameData;

import javax.xml.crypto.Data;
import java.util.ArrayList;
import java.util.HashSet;

public interface GameDAO {
    public GameData createGame(GameData gameData) throws DataAccessException;
    public GameData getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames() throws DataAccessException;
    public void updateGamePlayer(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException;

    void isOver(int gameID) throws DataAccessException;

    public void clearGames() throws DataAccessException;
    void deleteGame(int gameID) throws DataAccessException;
    public void updateGame(int gameID, ChessGame newGame) throws DataAccessException;
    public void removePlayer(int gameID, String username,String colorSwitch) throws DataAccessException;
}
