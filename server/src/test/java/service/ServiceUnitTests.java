package service;

import dataaccess.*;
import exception.AlreadyTakenException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;
import model.UserData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mindrot.jbcrypt.BCrypt;
import server.WebSocketSessionsManager;

import static org.junit.jupiter.api.Assertions.*;

public class ServiceUnitTests {
    AuthDAO authData = new AuthMemoryDAO();
    GameDAO gameData = new GameMemoryDAO();
    UserDAO userData = new UserMemoryDAO();
    DataAccessContainer dataContainer = new DataAccessContainer(authData, gameData, userData);
    WebSocketSessionsManager sessionsManager = new WebSocketSessionsManager();
    ServiceContainer services = new ServiceContainer(dataContainer,sessionsManager);

    private UserData userJoe;
    private AuthData auth;

    @BeforeEach
    void clear() throws Exception {
        services.clearService.clearAllData();
        userJoe = new UserData("joe", "123", "joe@joe.com");
        auth = services.registerService.register(userJoe);

    }

    @Test
    void registerUser() throws Exception {
        var userVerified = userData.getUser("joe");
        assertEquals(userJoe.username(), userVerified.username());
//        assertTrue(BCrypt.checkpw(userVerified.password(), BCrypt.hashpw(userJoe.password(),BCrypt.gensalt())));
    }

    @Test
    void registerAlreadyTakenUser() throws Exception {
        var userJoeClone = new UserData("joe", "123", "joe@joe.com");
        Assertions.assertThrows(AlreadyTakenException.class, () -> {
            services.registerService.register(userJoeClone);
        });
    }

    @Test
    void logoutUser() throws Exception {
        services.logoutService.logout(auth.authToken());
        var noUser = authData.getAuthDatabase();
        assertEquals(0,noUser.size());
    }

    @Test
    void logoutUserTwice() throws Exception {
        services.logoutService.logout(auth.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            services.logoutService.logout(auth.authToken());
        });
    }

    @Test
    void loginUser() throws Exception {
        services.logoutService.logout(auth.authToken());
        services.loginService.login(userJoe.username(),userJoe.password());
        assertEquals(1,authData.getAuthDatabase().size());
    }

    @Test
    void loginUserWrongPassword() throws Exception {
        services.logoutService.logout(auth.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            services.loginService.login(userJoe.username(), "YEEEEEEEEHAWW");
        });
    }

    @Test
    void createGameID() throws Exception {
        int gameID = services.createService.makeGameID();
        assertInstanceOf(Integer.class,gameID);
    }

    @Test
    void createGameAuthenticate() throws Exception {
        var authentic = services.createService.authenticate(auth.authToken());
        assertInstanceOf(AuthData.class, authentic);
    }

    @Test
    void createGameAuthenticateWithWrongToken() throws Exception {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            services.createService.authenticate(auth.authToken()+"s");;
        });
    }

    @Test
    void createGame() throws Exception {
        GameData game = new GameData(1,null,null,"gamerz",null,false);
        services.createService.createNewGame(game);
        assertEquals(1,gameData.listGames().size());
    }

    @Test
    void createGameNoName() throws Exception {
        GameData gameBad = new GameData(1,null,null,null,null,false);
        Assertions.assertThrows(BadRequestException.class, () -> {
            services.createService.createNewGame(gameBad);
        });
    }

    @Test
    void createGameUniqueID() throws Exception {
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        GameData game2 = new GameData(1,null,null,"game2",null,false);
        services.createService.createNewGame(game1);
        assertFalse(services.createService.checkUniqueID(1));
        assertTrue(services.createService.checkUniqueID(555));
    }

    @Test
    void listGames() throws Exception {
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        services.createService.createNewGame(game1);
        GameData game2 = new GameData(2,null,null,"game2",null,false);
        services.createService.createNewGame(game2);
        GameData game3 = new GameData(3,null,null,"game3",null,false);
        services.createService.createNewGame(game3);
        assertEquals(3, services.listService.list(auth.authToken()).size());
    }

    @Test
    void listGamesUnauthorized() throws Exception {
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        services.createService.createNewGame(game1);
        GameData game2 = new GameData(2,null,null,"game2",null,false);
        services.createService.createNewGame(game2);
        GameData game3 = new GameData(3,null,null,"game3",null,false);
        services.createService.createNewGame(game3);
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            services.listService.list("cheemsburger");
        });
    }

    @Test
    void joinGameFindGame() throws Exception {
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        services.createService.createNewGame(game1);
        assertInstanceOf(GameData.class, services.joinService.findGame(1));
    }

    @Test
    void joinGameCannotFindGame() throws Exception {
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        services.createService.createNewGame(game1);
        Assertions.assertThrows(BadRequestException.class, () -> {
            services.joinService.findGame(2);
        });
    }

    @Test
    void joinGameAuthenticate() throws Exception {
        assertInstanceOf(AuthData.class,services.joinService.authenticate(auth.authToken()));
    }

    @Test
    void joinGameNotAuthenticated() throws Exception {
        services.logoutService.logout(auth.authToken());
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            services.joinService.authenticate(auth.authToken());
        });
    }

    @Test
    void joinGame() throws Exception {
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        services.joinService.addPlayer(game1,"WHITE", userJoe.username());
        assertEquals("joe",gameData.getGame(1).whiteUsername());
    }

    @Test
    void joinGameBadColor() throws Exception {
        UserData userBob = new UserData("bob","123","bob@bob.com");
        GameData game1 = new GameData(1,null,null,"game1",null,false);
        services.joinService.addPlayer(game1,"WHITE", userJoe.username());
        Assertions.assertThrows(BadRequestException.class, () -> {
            services.joinService.addPlayer(game1,"BLUE",userBob.username());
        });
    }


//    @Test
//    void listGames() throws Exception {
//        services.
//    }

}
