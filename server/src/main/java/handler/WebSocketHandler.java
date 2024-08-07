package handler;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPosition;
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

import static chess.ChessPiece.PieceType.KNIGHT;
import static chess.ChessPiece.PieceType.PAWN;
import static websocket.commands.UserGameCommand.CommandType.*;
import static websocket.messages.ServerMessage.ServerMessageType.*;

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
            var messageAsJavaObject = new Gson().fromJson(message, UserGameCommand.class);
            var command = messageAsJavaObject.getCommandType();
            var authToken = messageAsJavaObject.getAuthToken();
            int gameID = messageAsJavaObject.getGameID();
            ChessGame game;
            GameData gameData;
            try {
                gameData = data.gameData.getGame(gameID);}
            catch (DataAccessException ex) {
                send(session, new Gson().toJson(
                        new ErrorMessage(ERROR, "Error : doesn't contain that game")));
                return;}
            AuthData authData;
            try {
                authData = data.authData.getAuth(authToken);}
            catch (UnauthorizedException ex){
                send(session, new Gson().toJson(
                        new ErrorMessage(ERROR, "Error : bad auth token")));
                return;}
            game = gameData.game();
            switch (command) {
                case CONNECT:
                    handleConnect(message,authToken,session,gameData,gameID,game);
                    break;
                case MAKE_MOVE:
                    handleMakeMove(gameData,session,message,authToken,game,gameID);
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
                        var notification = new NotificationMessage(
                                NOTIFICATION,usernameCurrent+" left the game!");
                        broadcast(notification,session,gameID);}
                    catch (UnauthorizedException ex) {
                        send(session,new Gson().toJson(
                                new ErrorMessage(ERROR,"you aren't allowed to access that game!"),
                                ErrorMessage.class));}
                    break;
                case RESIGN:
                    handleResign(gameData,session,authToken,message,gameID);
                    break;}}
        catch (Exception ex) {
            System.out.println(ex.getMessage());}}

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
                var messageToSendToClients = String.format("%s has joined the game as"+colorMessage, username);// needs to say as player or observer
                var notification = new NotificationMessage(
                        NOTIFICATION, messageToSendToClients);
                broadcast(notification, session, gameID);
            } else {
                send(session, new Gson().toJson(
                        new LoadGameMessage(LOAD_GAME, null, game), LoadGameMessage.class));
                var messageToSendToClient = String.format("%s is observing the game ", username); // needs to say as player or observer
                var notification = new NotificationMessage(
                        NOTIFICATION, messageToSendToClient);
                broadcast(notification, session, gameID);
            }
        }
        catch (DataAccessException ex) {
            send(session, new Gson().toJson(new ErrorMessage(ERROR, "Error: this game doesn't exist!"), ErrorMessage.class));
        }
    }

    private void handleMakeMove(GameData gameData, Session session, String message,String authToken,ChessGame game,int gameID) throws Exception {
        if (gameData.isOver()){
            send(session, new Gson().toJson(new ErrorMessage(ERROR, "Error: this is over!"), ErrorMessage.class));
            return;
        }
        var moveCommand = new Gson().fromJson(message,MakeMoveCommand.class);
        try {
            String usernameCurrent = data.authData.getAuth(authToken).username();
            String usernameWhite = gameData.whiteUsername();
            String usernameBlack = gameData.blackUsername();
            if (((game.getTeamTurn()==ChessGame.TeamColor.WHITE) && (Objects.equals(usernameCurrent, usernameWhite)))
                    || ((game.getTeamTurn()== ChessGame.TeamColor.BLACK) && (Objects.equals(usernameCurrent,usernameBlack)))) {
                service.makeMove(moveCommand, data, game); // try making the move
                ChessGame newGame = data.gameData.getGame(gameID).game();
                ChessGame.TeamColor color = newGame.getTeamTurn();

                var loadMessage = new LoadGameMessage(LOAD_GAME, null, newGame);// send new game to player, all session members
                send(session, new Gson().toJson(loadMessage, LoadGameMessage.class));
                broadcast(loadMessage, session, gameID);
                String moveAsString = moveCommand.getChessMove().toString();// broadcast which move has been made
                var moveNotification = new NotificationMessage(NOTIFICATION, moveAsString);
                broadcast(moveNotification, session, gameID);

                if (newGame.isInStalemate(color)) {
                    var stalemateMessage = new NotificationMessage(NOTIFICATION,"stalemate! the game is over!");
                    send(session,new Gson().toJson(stalemateMessage,NotificationMessage.class));
                    broadcast(stalemateMessage,session,gameID);
                    service.gameIsOver(gameID,data);
                }
                else if (newGame.isInCheckmate(color)){
                    String displayUsername=usernameBlack;
                    if (newGame.getTeamTurn()==ChessGame.TeamColor.WHITE){
                        displayUsername = usernameWhite;
                    }
                    var checkmateMessageRoot = new NotificationMessage(NOTIFICATION,displayUsername+" has been checkmated! you won!");
                    send(session,new Gson().toJson(checkmateMessageRoot,NotificationMessage.class));
                    var checkmateMessageOthers = new NotificationMessage(NOTIFICATION,displayUsername+" has been checkmated! the game is over!");
                    broadcast(checkmateMessageOthers,session,gameID);
                    service.gameIsOver(gameID,data);
                }
                else if (newGame.isInCheck(color)) {
                    String displayUsername=usernameBlack;
                    if (newGame.getTeamTurn()==ChessGame.TeamColor.WHITE){
                        displayUsername = usernameWhite;
                    }
                    var checkMessageRoot = new NotificationMessage(NOTIFICATION,displayUsername+" is in check!");
                    send(session,new Gson().toJson(checkMessageRoot,NotificationMessage.class));
                    var checkMessageOthers = new NotificationMessage(NOTIFICATION,displayUsername+" is in check!");
                    broadcast(checkMessageOthers,session,gameID);
                }
            }
            else {
                send(session,new Gson().toJson(new ErrorMessage(ERROR,"Error: it's not your turn!"),ErrorMessage.class));}}
        catch (BadRequestException ex){
            ErrorMessage errorMessage = new ErrorMessage(ERROR,"that move is illegal. try again.");
            send(session,new Gson().toJson(errorMessage,ErrorMessage.class));}
    }

    private void handleResign(GameData gameData, Session session, String authToken, String message, int gameID) throws Exception {
        if (gameData.isOver()){
            send(session, new Gson().toJson(
                    new ErrorMessage(ERROR, "Error: it's over, Anakin! I have the high ground!"), ErrorMessage.class));
            return;}
        String usernameCurrentResign = data.authData.getAuth(authToken).username();
        String usernameWhiteResign = gameData.whiteUsername();
        String usernameBlackResign = gameData.blackUsername();
        if ((Objects.equals(usernameCurrentResign, usernameWhiteResign))
                ||  (Objects.equals(usernameCurrentResign,usernameBlackResign))) {
            var resignCommand = new Gson().fromJson(message, ResignCommand.class);// try making the move
            try {
                service.resignGame(resignCommand, data);
                send(session, new Gson().toJson(
                        new NotificationMessage(NOTIFICATION, "you resigned the game!"),
                        NotificationMessage.class));
                var notification = new NotificationMessage(
                        NOTIFICATION, usernameCurrentResign + " left the game!");
                broadcast(notification, session, gameID);
            } catch (RuntimeException ex) {
                send(session, new Gson().toJson(
                        new ErrorMessage(ERROR, "you can't resign that game!"),
                        ErrorMessage.class));}
        }
        else {
            send(session,new Gson().toJson(
                    new ErrorMessage(ERROR,"you aren't allowed to resign the game!"),
                    ErrorMessage.class));}
    }

    private String chessMoveToStringDescription(ChessGame newGame,ChessMove move) {
        ChessPosition position = move.getEndPosition();
        String first = switch(newGame.getBoard().getPiece(position).getPieceType()){
            case PAWN->"";
            case KNIGHT->"N";
            case KING -> "K";
            case QUEEN -> "Q";
            case BISHOP ->"B";
            case ROOK -> "R";
        };
        String second = switch(position.getColumn()){
            case 1->"a";
            case 2->"b";
            case 3->"c";
            case 4->"d";
            case 5->"e";
            case 6->"f";
            case 7->"g";
            case 8->"h";
            default -> throw new IllegalStateException("Unexpected value: " + position.getColumn());
        };
        String third = String.valueOf(position.getRow());
        String fourth;
        if (newGame.isInCheck(newGame.getTeamTurn())){fourth = "+";}
        else if (newGame.isInCheckmate(newGame.getTeamTurn())) {fourth = "#";}
        else {fourth = "";}
        return first+second+third+fourth;
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