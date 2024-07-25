package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class GameSQLDAO implements GameDAO {
    @Override
    public GameData createGame(GameData gameData) {
        return null;
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public ArrayList<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGamePlayer(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {

    }

    @Override
    public void clearGames() {

    }

    @Override
    public void deleteGame(int gameID) {

    }
}
