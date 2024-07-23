package handler;

import chess.ChessGame;
import com.google.gson.Gson;
import exception.BadRequestException;
import exception.UnauthorizedException;
import message.CreateGameRequest;
import message.CreateGameReturn;
import message.ErrorMessage;
import model.GameData;
import service.CreateGameService;
import spark.Request;
import spark.Response;
import spark.Route;

public class CreateGameHandler implements Route {

    private CreateGameService service;

    public CreateGameHandler(CreateGameService createGameService){
        this.service = createGameService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
            var gameNameReq = new Gson().fromJson(req.body(), CreateGameRequest.class);
            String gameName = gameNameReq.gameName();
            if (gameName==null) {throw new BadRequestException("Error: bad request");}
            var auth = service.authenticate(authToken);
            // need to check auth token if user is verified
            int gameID = service.makeGameID();
            boolean isAddableGame = service.checkUniqueID(gameID);
            if (isAddableGame) {
                service.createNewGame(new GameData(gameID,null,null,gameName,new ChessGame()));
                res.status(200);
                return new Gson().toJson(new CreateGameReturn(gameID));
            }
            throw new Exception("couldn't be added..?");
        }
        catch (BadRequestException badRequest) {
            res.status(400);
            return new Gson().toJson(new ErrorMessage(badRequest.getMessage()));
        }
        catch (UnauthorizedException unauthorized) {
            res.status(401);
            return new Gson().toJson(new ErrorMessage(unauthorized.getMessage()));
        }
        catch (Exception otherException) {
            res.status(500);
            return new Gson().toJson(new ErrorMessage(otherException.getMessage()));
        }
    }

}
