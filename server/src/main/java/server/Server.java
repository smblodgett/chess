package server;

import handler.*;
import service.ClearDataService;
import spark.*;

public class Server {
    private ClearDataService clearService;


    Server(){

    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        // list game
        Spark.get("/game",new ListGameHandler());
        // create new game
        Spark.post("/game",new CreateGameHandler());
        // join a game
        Spark.post("/game", new JoinGameHandler());
        // register new user
        Spark.post("/user",new RegisterHandler());
        // login user
        Spark.post("/session",new LoginHandler());
        // logout user
        Spark.post("/session",new LogoutHandler());
        // delete data
        Spark.delete("/db",new ClearDataHandler());


        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}