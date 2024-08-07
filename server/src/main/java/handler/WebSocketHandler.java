package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import server.WebSocketSessionsManager;
import service.WebSocketService;
import websocket.commands.*;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.ERROR;
import static websocket.messages.ServerMessage.ServerMessageType.LOAD_GAME;

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
            ChessGame game;
            GameData gameData;
            try {
                gameData = data.gameData.getGame(gameID);
            }
            catch (DataAccessException ex) {
                send(session, new Gson().toJson(
                        new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error : doesn't contain that game")));
                return;
            }
            AuthData authData;
            try {
                authData = data.authData.getAuth(authToken);
            }
            catch (UnauthorizedException ex){
                send(session, new Gson().toJson(
                        new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "Error : bad auth token")));
                return;
            }

            game = gameData.game();

            switch (command) {
                case CONNECT:
                    handleConnect(message,authToken,session,gameData,gameID,game);
                    break;
                case MAKE_MOVE:
                    if (gameData.isOver()){
                        send(session, new Gson().toJson(new ErrorMessage(ERROR, "Error: this is over!"), ErrorMessage.class));
                    }
                    var moveCommand = new Gson().fromJson(message,MakeMoveCommand.class);
                    try {
                        String usernameCurrent = data.authData.getAuth(authToken).username();
                        String usernameWhite = gameData.whiteUsername();
                        String usernameBlack = gameData.blackUsername();
                        if (((game.getTeamTurn()==ChessGame.TeamColor.WHITE) && (Objects.equals(usernameCurrent, usernameWhite)))
                            || ((game.getTeamTurn()== ChessGame.TeamColor.BLACK) && (Objects.equals(usernameCurrent,usernameBlack)))) {
                            // try making the move
                            service.makeMove(moveCommand, data, game);
                            ChessGame newGame = data.gameData.getGame(gameID).game();
                            // send new game to player, all session members
                            var loadMessage = new LoadGameMessage(LOAD_GAME, null, newGame);
                            send(session, new Gson().toJson(loadMessage, LoadGameMessage.class));
                            broadcast(loadMessage, session, gameID);
                            // broadcast which move has been made
                            String moveAsString = moveCommand.getChessMove().toString();
                            var moveNotification = new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, moveAsString);
                            broadcast(moveNotification, session, gameID);
                        }
                        else {
                            send(session,new Gson().toJson(new ErrorMessage(ERROR,"Error: it's not your turn!"),ErrorMessage.class));
                        }
                    }
                    catch (BadRequestException ex){
                        ErrorMessage errorMessage = new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"that move is illegal. try again.");
                        send(session,new Gson().toJson(errorMessage,ErrorMessage.class));
                    }
                    break;
                case LEAVE:
                    var leaveCommand = new Gson().fromJson(message, LeaveCommand.class);
                    String usernameCurrent = data.authData.getAuth(authToken).username();
                    String usernameWhite = gameData.whiteUsername();
                    String usernameBlack = gameData.blackUsername();
                    String colorSwitch=null;
                    if (Objects.equals(usernameCurrent, usernameWhite)){colorSwitch="w";}
                    else if (Objects.equals(usernameCurrent, usernameBlack)){colorSwitch="b";}
                    try {
                        if (colorSwitch!=null) {service.leaveGame(leaveCommand,data,colorSwitch,session);}
//                        send(session,new Gson().toJson(new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION,"you left the game!"),NotificationMessage.class));
                        var notification = new NotificationMessage(
                                ServerMessage.ServerMessageType.NOTIFICATION,leaveCommand.username+" left the game!");
                        broadcast(notification,session,gameID);
                    }
                    catch (UnauthorizedException ex) {
                        send(session,new Gson().toJson(
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"you aren't allowed to access that game!"),ErrorMessage.class));
                    }
                    break;
                case RESIGN:
                    if (gameData.isOver()){
                        send(session, new Gson().toJson(
                                new ErrorMessage(ERROR, "Error: it's over, Anakin! I have the high ground!"), ErrorMessage.class));
                        return;
                    }
                    String usernameCurrentResign = data.authData.getAuth(authToken).username();
                    String usernameWhiteResign = gameData.whiteUsername();
                    String usernameBlackResign = gameData.blackUsername();
                    if ((Objects.equals(usernameCurrentResign, usernameWhiteResign))
                            ||  (Objects.equals(usernameCurrentResign,usernameBlackResign))) {
                        // try making the move
                        var resignCommand = new Gson().fromJson(message, ResignCommand.class);
                        try {
                            service.resignGame(resignCommand, data);
                            send(session, new Gson().toJson(
                                    new NotificationMessage(ServerMessage.ServerMessageType.NOTIFICATION, "you resigned the game!"),
                                    NotificationMessage.class));
                            var notification = new NotificationMessage(
                                    ServerMessage.ServerMessageType.NOTIFICATION, resignCommand.username + " left the game!");
                            broadcast(notification, session, gameID);
                        } catch (RuntimeException ex) {
                            send(session, new Gson().toJson(
                                    new ErrorMessage(ServerMessage.ServerMessageType.ERROR, "you can't resign that game!"),
                                    ErrorMessage.class));
                        }
                    }
                    else {
                        send(session,new Gson().toJson(
                                new ErrorMessage(ServerMessage.ServerMessageType.ERROR,"you aren't allowed to resign the game!"),
                                ErrorMessage.class));
                    }
                    break;
            }
        }
        catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

    }

    private void handleConnect(String message, String authToken, Session session, GameData gameData, int gameID,ChessGame game) throws Exception {
        var connectCommand = new Gson().fromJson(message,ConnectCommand.class);
        service.connect(connectCommand, session);
        String username = data.authData.getAuth(authToken).username();
        String colorMessage ="";
        try {
//                        if (gameData==null) System.out.println("hsdfhsdf");
            if (Objects.equals(gameData.whiteUsername(), username)) {
                connectCommand = new ConnectCommand(CONNECT, authToken, gameID, ChessGame.TeamColor.WHITE);
            } if (Objects.equals(gameData.blackUsername(), username)) {
                connectCommand = new ConnectCommand(CONNECT, authToken, gameID, ChessGame.TeamColor.BLACK);
            }
            if (connectCommand.getColor() != null) {
                if (connectCommand.getColor() == ChessGame.TeamColor.WHITE) {
                    colorMessage = "white";
                }
                if (connectCommand.getColor() == ChessGame.TeamColor.BLACK) {
                    colorMessage = "black";
                }
                // send loaded game to player
                send(session, new Gson().toJson(
                        new LoadGameMessage(LOAD_GAME, null, game), LoadGameMessage.class));
                var messageToSendToClients = String.format("%s has joined the game as" + colorMessage, username); // needs to say as player or observer
                var notification = new NotificationMessage(
                        NotificationMessage.ServerMessageType.NOTIFICATION, messageToSendToClients);
                broadcast(notification, session, gameID);
            } else {
                send(session, new Gson().toJson(
                        new LoadGameMessage(LOAD_GAME, null, game), LoadGameMessage.class));
                var messageToSendToClient = String.format("%s is observing the game ", username); // needs to say as player or observer
                var notification = new NotificationMessage(
                        NotificationMessage.ServerMessageType.NOTIFICATION, messageToSendToClient);
                broadcast(notification, session, gameID);
            }
        }
        catch (DataAccessException ex) {
            send(session, new Gson().toJson(new ErrorMessage(ERROR, "Error: this game doesn't exist!"), ErrorMessage.class));
        }
    }

    private void broadcast(ServerMessage message, Session session,int gameID) {
        Set<Session> sessions = sessionsManager.getAllSessions().get(gameID);
        Gson gson = new Gson();
        String messageJson = gson.toJson(message,message.getClass());

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