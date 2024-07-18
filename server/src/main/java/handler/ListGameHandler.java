package handler;

import service.ListGameService;
import spark.*;

public class ListGameHandler implements Route {

    private ListGameService service;

    public ListGameHandler(ListGameService listGameService){
        this.service=listGameService;
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
        return null;
    }
}
