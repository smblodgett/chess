package handler;

import com.google.gson.Gson;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import message.ErrorMessage;
import model.UserData;
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
        try {
            var userData = new Gson().fromJson(req.body(), UserData.class);
            var authData = service.login(userData.username(),userData.password());
            res.status(200);
            return new Gson().toJson(authData);
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
