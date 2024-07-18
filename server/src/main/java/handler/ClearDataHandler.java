package handler;

import spark.Request;
import spark.Response;
import spark.Route;

public class ClearDataHandler implements Route {

    @Override
    public Object handle(Request request, Response response) throws Exception {
        service.ClearDataService();

        return "";
    }
}
