package handler;

import service.LoginService;
import service.LogoutService;
import spark.*;

public class LoginHandler implements Route {

    private LoginService service;

    public LoginHandler(LoginService loginService){
        this.service=loginService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        return null;
    }
}
