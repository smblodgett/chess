package handler;

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
import websocket.messages.ServerMessage;

import java.io.IOException;

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

    @OnWebSocketConnect
    public void onConnect(Session session){

    }

    @OnWebSocketClose
    public void onClose(Session session){

    }

    @OnWebSocketError
    public void onError(Session session){

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message){
        try {
            Gson gson = new Gson();
            var messageAsJavaObject = gson.fromJson(message, UserGameCommand.class);
            var command = messageAsJavaObject.getCommandType();
            var authToken = messageAsJavaObject.getAuthToken();

            switch (command) {
                case CONNECT:
                    service.connect((ConnectCommand) messageAsJavaObject, session);
                    String username = data.authData.getAuth(authToken).username();
                    var messageToSendToClient = String.format("%s has joined the game", username);
                    var notification = new ServerMessage(ServerMessage.ServerMessageType.LOAD_GAME, messageToSendToClient);
                    sessionsManager.broadcast(username, notification,session);
                    break;
                case MAKE_MOVE -> service.makeMove((MakeMoveCommand) messageAsJavaObject, session);
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





}
