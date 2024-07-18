package handler;

import service.ClearDataService;
import spark.Request;
import spark.Response;
import spark.Route;

public class ClearDataHandler implements Route {

    ClearDataService service;

    public ClearDataHandler(ClearDataService service){
        this.service = service;
    }

    @Override
    public Object handle(Request req, Response res) throws Exception {
        service.clearAllData();
        res.status(200);
        return "";
    }
}
