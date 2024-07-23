package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.HashSet;

public interface GameDAO {
    public GameData createGame(GameData gameData);
    public GameData getGame(int gameID) throws DataAccessException;
    public ArrayList<GameData> listGames();
    public void updateGamePlayer(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException;
    public void clearGames();
    void deleteGame(int gameID);
}
