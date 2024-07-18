package handler;

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
        return null;
    }
}
