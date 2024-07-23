package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import message.CreateGameRequest;
import message.CreateGameReturn;
import message.ErrorMessage;
import message.JoinGameRequest;
import model.AuthData;
import model.GameData;
import service.JoinGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class JoinGameHandler implements Route {

    private JoinGameService service;

    public JoinGameHandler(JoinGameService joinGameService){
        this.service=joinGameService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
            var gameJoinReq = new Gson().fromJson(req.body(), JoinGameRequest.class);
            String color = gameJoinReq.playerColor();
            int gameID = gameJoinReq.gameID();
            if (color==null) throw new BadRequestException("Error: bad request");
            AuthData auth = service.authenticate(authToken);
            String username = auth.username();
            var gameData = service.findGame(gameID);
            if (color.equals("BLACK") && gameData.blackUsername()!=null) throw new AlreadyTakenException("Error: already taken");
            if (color.equals("WHITE") && gameData.blackUsername()!=null) throw new AlreadyTakenException("Error: already taken");
            GameData addedGame = service.addPlayer(gameData,color, username);
            res.status(200);
            return "";

        }
        catch (BadRequestException badRequest) {
            res.status(400);
            return new Gson().toJson(new ErrorMessage(badRequest.getMessage()));
        }
        catch (UnauthorizedException | JsonSyntaxException unauthorized) {
            res.status(401);
            return new Gson().toJson(new ErrorMessage("Error: unauthorized"));
        }
        catch (AlreadyTakenException alreadyTaken) {
            res.status(403);
            return new Gson().toJson(new ErrorMessage(alreadyTaken.getMessage()));
        }
        catch (Exception otherException) {
            res.status(500);
            return new Gson().toJson(new ErrorMessage(otherException.getMessage()));
        }
    }
}
