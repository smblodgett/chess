package handler;

import com.google.gson.Gson;
import exception.UnauthorizedException;
import message.ErrorMessage;
import model.UserData;
import service.LogoutService;
import spark.Request;
import spark.Response;
import spark.Route;

import static dataaccess.AuthMemoryDAO.authDatabase;
import static dataaccess.UserMemoryDAO.userDatabase;

public class LogoutHandler implements Route {

    private LogoutService service;

    public LogoutHandler(LogoutService logoutService){
        this.service=logoutService;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        try {
            var authToken = new Gson().fromJson(req.headers("authorization"), String.class);
            service.logout(authToken);
            res.status(200);
            return "";
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
