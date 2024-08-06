package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.WebSocketSessionsManager;
import service.WebSocketService;
import websocket.commands.ConnectCommand;
import websocket.commands.LeaveCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
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
            ChessGame game = data.gameData.getGame(gameID).game();

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
                        broadcast(notification,session,gameID);
                        send(session,new Gson().toJson(new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,"joined game!",game)));
                    }
                    else {
                        var messageToSendToClient = String.format("%s is observing the game ", username); // needs to say as player or observer
                        var notification = new NotificationMessage(NotificationMessage.ServerMessageType.NOTIFICATION, messageToSendToClient);
                        broadcast(notification,session,gameID);
                    }
                    break;
                case MAKE_MOVE:
                    var moveCommand = new Gson().fromJson(message,MakeMoveCommand.class);
                    try {
                        // try making the move
                        service.makeMove(moveCommand,data, game);
                        ChessGame newGame = data.gameData.getGame(gameID).game();
                        // send new game to player, all session members
                        var loadMessage = new LoadGameMessage(ServerMessage.ServerMessageType.LOAD_GAME,"move made!",newGame);
                        send(session,new Gson().toJson(loadMessage,LoadGameMessage.class));
                        broadcast(loadMessage,session,gameID);
                        // broadcast which move has been made
                        String moveAsString = moveCommand.getChessMove().toString();
                        var moveNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,moveAsString);
                        broadcast(moveNotification,session,gameID);
                    }
                    catch (BadRequestException ex){
                        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"that move is illegal. try again.");
                        send(session,new Gson().toJson(errorMessage,ErrorMessage.class));
                    }
                    break;
                case LEAVE:
                    var leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    try {
                        service.leaveGame(leaveCommand,data);
                        send(session,new Gson().toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,"you left the game!"),NotificationMessage.class));
                        var notification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,leaveCommand.username+" left the game!");
                        broadcast(notification,session,gameID);
                    }
                    catch (DataAccessException ex) {
                        send(session,new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"there was a problem with the database"),ErrorMessage.class));
                    }
                    catch (UnauthorizedException ex) {
                        send(session,new Gson().toJson(new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"you aren't allowed to access that game!"),ErrorMessage.class));
                    }
                    break;
                case RESIGN:
                    break;
            }

        }
        catch (Exception ex) {
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
