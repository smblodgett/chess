package handler;

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
        return null;
    }
}
