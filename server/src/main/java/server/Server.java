package server;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.ResponseException;
import handler.*;
import service.ServiceContainer;
import spark.*;

import static dataaccess.DatabaseManager.createDatabase;

public class Server {

    private ServiceContainer services;


    public Server(){
        try {
            createDatabase();
        }
        catch (Exception ex) {
            System.out.println("unable to create database");
        }
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        var authData = new AuthSQLDAO();
        var gameData = new GameSQLDAO();
        var userData = new UserSQLDAO();
        var dataContainer = new DataAccessContainer(authData, gameData, userData);
        var services = new ServiceContainer(dataContainer); // make handler/service reference data statically
        this.addServices(services);

        // list game
        Spark.get("/game",new ListGameHandler(services.listService));
        // create new game
        Spark.post("/game",new CreateGameHandler(services.createService));
        // join a game
        Spark.put("/game", new JoinGameHandler(services.joinService));
        // register new user
        Spark.post("/user",new RegisterHandler(services.registerService));
        // login user
        Spark.post("/session",new LoginHandler(services.loginService));
        // logout user
        Spark.delete("/session",new LogoutHandler(services.logoutService));
        // delete data
        Spark.delete("/db",new ClearDataHandler(services.clearService));


        //This line initializes the server and can be removed once you have a functioning endpoint
        Spark.init();

        Spark.awaitInitialization();
        return Spark.port(); // possibly should be an int...?
    }

    public int port(){
        return Spark.port();
    }

    public void addServices(ServiceContainer services){
        this.services=services;
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}