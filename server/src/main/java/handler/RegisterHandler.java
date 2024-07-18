package handler;

import service.RegisterService;
import spark.Request;
import spark.Response;
import spark.Route;

public class RegisterHandler implements Route {

    private RegisterService service;

    public RegisterHandler(RegisterService registerService){
        this.service = registerService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return null;
    }
}
