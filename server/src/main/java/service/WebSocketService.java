package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
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

    public void makeMove(MakeMoveCommand command, Session session){

    }

    public void leaveGame(UserGameCommand command) {

    }

    public void resignGame(UserGameCommand command) {

    }
}
