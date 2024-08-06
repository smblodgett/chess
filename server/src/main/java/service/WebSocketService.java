package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import model.GameData;
import server.WebSocketSessionsManager;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import org.eclipse.jetty.websocket.api.Session;


public class WebSocketService {

    private WebSocketSessionsManager sessionsManager;

    public WebSocketService(WebSocketSessionsManager sessionsManager) {
        this.sessionsManager=sessionsManager;
    }

    public void connect(ConnectCommand command, Session session){
        int gameID = command.getGameID();
        sessionsManager.addSessionToGame(gameID,session);
    }

    public void makeMove(MakeMoveCommand command,DataAccessContainer data,ChessGame game) throws BadRequestException {
        int gameID = command.getGameID();
        ChessMove move = command.getChessMove();
        try {
            game.makeMove(move);
            data.gameData.updateGame(gameID,game);
        }
        catch (InvalidMoveException | DataAccessException ex){
            throw new BadRequestException("can't move there");
        }
    }

    public void leaveGame(UserGameCommand command) {

    }

    public void resignGame(UserGameCommand command) {

    }
}
