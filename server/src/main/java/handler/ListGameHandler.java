package handler;

import com.google.gson.Gson;
import exception.UnauthorizedException;
import message.message.ErrorMessage;
import message.message.ListGameReturn;
import service.ListGameService;
import spark.*;
import response.ListGameResponse;

public class ListGameHandler implements Route {

    private ListGameService service;

    public ListGameHandler(ListGameService listGameService){
        this.service=listGameService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
            var games = service.list(authToken);
            var gamesReturn = new ListGameReturn(games).removeChessBoards();
            res.status(200);
            return new Gson().toJson(new ListGameResponse(gamesReturn));
        }
        catch (UnauthorizedException unauthorized) {
            res.status(401);
            return new Gson().toJson(new ErrorMessage(unauthorized.getMessage()));
        }
        catch (Exception otherException){
            res.status(500);
            return new Gson().toJson(new ErrorMessage(otherException.getMessage()));
        }
    }
}
