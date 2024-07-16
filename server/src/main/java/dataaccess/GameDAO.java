package dataaccess;

import chess.ChessGame;

public interface GameDAO {
    public ChessGame createGame();
    public ChessGame getGame(String gameID);
    public ChessGame[] listGames();
    public void updateGame(String gameID);
    public void clearGames();
}
