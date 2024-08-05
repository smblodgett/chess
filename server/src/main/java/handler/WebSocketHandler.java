package handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.WebSocketSessionsManager;
import service.WebSocketService;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

import static websocket.commands.UserGameCommand.CommandType.*;

@WebSocket
public class WebSocketHandler {

    private WebSocketService service;
    private WebSocketSessionsManager sessionsManager;

    public WebSocketHandler(WebSocketService webSocketService, WebSocketSessionsManager manager){
        this.service=webSocketService;
        this.sessionsManager=manager;
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

        Gson gson = new Gson();
        var messageAsJavaObject = gson.fromJson(message, UserGameCommand.class);
        var command = messageAsJavaObject.getCommandType();

        switch (command){
            case CONNECT -> service.connect((ConnectCommand) messageAsJavaObject);
            case MAKE_MOVE -> service.makeMove((MakeMoveCommand) messageAsJavaObject);
        }

        if (command == CONNECT){
            service.connect((ConnectCommand) messageAsJavaObject);
        }
        else if (command == MAKE_MOVE) {
            service.makeMove((MakeMoveCommand) messageAsJavaObject);
        }
        else if (command == LEAVE){
            service.leaveGame(messageAsJavaObject);
        }
        else if (command == RESIGN){
            service.resignGame(messageAsJavaObject);
        }

    }





}
