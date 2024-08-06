package client;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSFacade extends Endpoint {

    Session session;
    NotificationHandler notificationHandler;
    ErrorHandler errorHandler;
    LoadGameMessageHandler loadGameHandler;
    public boolean isOnMessage = false;


    public WSFacade(String url, NotificationHandler notificationHandler, ErrorHandler errorHandler, LoadGameMessageHandler loadGameHandler)  {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.notificationHandler = notificationHandler;
            this.errorHandler = errorHandler;
            this.loadGameHandler = loadGameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    isOnMessage=true;
                    handleMessage(message);
                }
            });
        }  catch (URISyntaxException | DeploymentException | IOException ex) {
            throw new RuntimeException(ex);
        }
    }

    private void handleMessage(String message) {
        ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case LOAD_GAME:
                LoadGameMessage loadGameMessage = new Gson().fromJson(message, LoadGameMessage.class);
//                loadGameHandler.notify(loadGameMessage);
                loadGameHandler.updateGame(loadGameMessage);
                break;
            case NOTIFICATION:
                NotificationMessage notificationMessage = new Gson().fromJson(message, NotificationMessage.class);
                notificationHandler.notify(notificationMessage);
                break;
            case ERROR:
                ErrorMessage errorMessage = new Gson().fromJson(message, ErrorMessage.class);
                errorHandler.notify(errorMessage);
                break;
            default:
                break;
        }
    }

    //Endpoint requires this method, but you don't have to do anything
    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connectToGame(String authToken, int gameID, ChessGame.TeamColor color)  {
        try {
            var action = new ConnectCommand(ConnectCommand.CommandType.CONNECT,authToken,gameID,color);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println("problem with connect to game in WSFacade");
        }
    }

    public boolean getIsOnMessage() {
        return isOnMessage;
    }

    public void setIsOnMessage(boolean bool){
        isOnMessage=bool;
    }

    public void makeMove(String authToken, int gameID, ChessMove move)  {
        try {
            var action = new MakeMoveCommand(MakeMoveCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
        } catch (IOException ex) {
            System.out.println("something wrong with make move in WSFacade");
        }
    }

    public void leaveGame(String authToken, int gameID,String username) {
        try {
            var action = new LeaveCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID,null,username);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resign(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID,null);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
