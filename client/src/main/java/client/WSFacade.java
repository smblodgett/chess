package client;


import chess.ChessGame;
import chess.ChessMove;
import com.google.gson.Gson;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WSFacade extends Endpoint {

    Session session;
    ServerMessageHandler serverMessageHandler;


    public WSFacade(String url, ServerMessageHandler serverMessageHandler)  {
        try {
            url = url.replace("http", "ws");
            URI socketURI = new URI(url + "/ws");
            this.serverMessageHandler = serverMessageHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            //set message handler
            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage serverMessage = new Gson().fromJson(message, ServerMessage.class);
                    serverMessageHandler.notify(serverMessage);
                }
            });
        }  catch (URISyntaxException | DeploymentException | IOException ex) {
            throw new RuntimeException(ex);
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

    public void makeMove(String authToken, int gameID, ChessMove move)  {
        try {
            var action = new MakeMoveCommand(MakeMoveCommand.CommandType.MAKE_MOVE, authToken, gameID, move);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException ex) {
            System.out.println("something wrong with make move in WSFacade");
        }
    }

    public void leaveGame(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void resign(String authToken, int gameID) {
        try {
            var action = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
            this.session.getBasicRemote().sendText(new Gson().toJson(action));
            this.session.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
