package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.UnauthorizedException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.WebSocketSessionsManager;
import service.WebSocketService;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;

import static websocket.commands.UserGameCommand.CommandType.*;

@WebSocket
public class WebSocketHandler {

    private WebSocketService service;
    private WebSocketSessionsManager sessionsManager;
    private DataAccessContainer data;

    public WebSocketHandler(WebSocketSessionsManager manager,DataAccessContainer data){
        this.sessionsManager=manager;
        this.service=new WebSocketService(manager);
        this.data=data;
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        try {
            Gson gson = new Gson();
            var messageAsJavaObject = gson.fromJson(message, UserGameCommand.class);
            var command = messageAsJavaObject.getCommandType();
            var authToken = messageAsJavaObject.getAuthToken();
            int gameID = messageAsJavaObject.getGameID();

            switch (command) {
                case CONNECT:
                    var connectCommand = new Gson().fromJson(message,ConnectCommand.class);
                    service.connect(connectCommand, session);
                    String username = data.authData.getAuth(authToken).username();
                    String colorMessage ="";
                    if (connectCommand.getColor()!=null){
                        if (connectCommand.getColor()== ChessGame.TeamColor.WHITE){colorMessage = "white";}
                        if (connectCommand.getColor()== ChessGame.TeamColor.BLACK){colorMessage = "black";}
                        var messageToSendToClient = String.format("%s has joined the game as"+colorMessage, username); // needs to say as player or observer
                        var notification = new NotificationMessage(NotificationMessage.ServerMessageType.NOTIFICATION, messageToSendToClient);
                        broadcast(username,notification,session);

                        send(session,new Gson().toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,"joined game!",)))
                    }
                    else {
                        var messageToSendToClient = String.format("%s is observing the game ", username); // needs to say as player or observer
                        var notification = new NotificationMessage(NotificationMessage.ServerMessageType.NOTIFICATION, messageToSendToClient);
                        broadcast(username,notification,session);
                    }
                    break;
                case MAKE_MOVE:
                    service.makeMove((MakeMoveCommand) messageAsJavaObject, session);

            }

            if (command == CONNECT) {
                service.connect((ConnectCommand) messageAsJavaObject, session);
            } else if (command == MAKE_MOVE) {
                service.makeMove((MakeMoveCommand) messageAsJavaObject, session);
            } else if (command == LEAVE) {
                service.leaveGame(messageAsJavaObject);
            } else if (command == RESIGN) {
                service.resignGame(messageAsJavaObject);
            }
        }
        catch (DataAccessException | UnauthorizedException | IOException ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void broadcast(ServerMessage message, Session session,int gameID) {
        Set<Session> sessions = sessionsManager.getAllSessions().get(gameID);
        Gson gson = new Gson();
        String messageJson = gson.toJson(message);

        for (Session s : sessions) {
            if (s.isOpen() && !s.equals(session)) {
                try {
                    s.getRemote().sendString(messageJson);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    public void send(Session session, String msg) throws Exception {
        session.getRemote().sendString(msg);
    }





}
