package service;

import chess.ChessGame;
import chess.ChessMove;
import chess.InvalidMoveException;
import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.GameData;
import server.WebSocketSessionsManager;
import websocket.commands.*;

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

    public void leaveGame(LeaveCommand command, DataAccessContainer data,String colorSwitch) throws UnauthorizedException, IllegalArgumentException {
        int gameID = command.getGameID();
        String authToken = command.getAuthToken();
        try {
            String username = data.authData.getAuth(authToken).username();
            data.gameData.removePlayer(gameID,username,colorSwitch);
        }
        catch (DataAccessException ex){
            throw new IllegalArgumentException("something messed with the data");
        }
        catch (UnauthorizedException ex){
            throw new UnauthorizedException("unauthorized access to leaveGame");
        }
    }

    public void resignGame(ResignCommand command,DataAccessContainer data) {
        int gameID = command.getGameID();
        try{
            data.gameData.isOver(gameID);
        }
        catch (DataAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
