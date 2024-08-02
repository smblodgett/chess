package handler;

import com.google.gson.Gson;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import message.message.ErrorMessage;
import model.UserData;
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
        try {
            var userData = new Gson().fromJson(req.body(), UserData.class);
            var authData = service.register(userData);
            res.status(200);
            return new Gson().toJson(authData);
        }
        catch (BadRequestException badRequest) {
            res.status(400);
            return new Gson().toJson(new ErrorMessage(badRequest.getMessage()));
        }
        catch (AlreadyTakenException alreadyTaken){
            res.status(403);
            return new Gson().toJson(new ErrorMessage(alreadyTaken.getMessage()));
        }
        catch (Exception otherException){
            res.status(500);
            return new Gson().toJson(new ErrorMessage(otherException.getMessage()));
        }
    }
}
