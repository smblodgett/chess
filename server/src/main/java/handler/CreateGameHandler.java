package handler;

import com.google.gson.Gson;
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
        var game = new Gson().fromJson(req.body(), GameData.class);
        // need to check auth token if user is verified
        int gameID = game.gameID();
        boolean isAddableGame = service.checkUniqueID(gameID);
        if (isAddableGame) {
            service.createNewGame(game);
            res.status(200);
            return new Gson().toJson(game);
        }
        return null;
    }

}
