package handler;

import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

public class LogoutHandler implements Route {

    private LogoutService service;

    public LogoutHandler(LogoutService logoutService){
        this.service=logoutService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return null;
    }
}
