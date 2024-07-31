package dataaccess;

import chess.ChessGame;
import exception.AlreadyTakenException;
import exception.UnauthorizedException;
import model.AuthData;
import model.UserData;
import model.GameData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static chess.ChessGame.TeamColor.WHITE;
import static org.junit.jupiter.api.Assertions.*;

public class DataAccessUnitTests {
    AuthDAO authData = new AuthSQLDAO();
    GameDAO gameData = new GameSQLDAO();
    UserDAO userData = new UserSQLDAO();

    private UserData userJoe;
    private AuthData auth;
    private GameData game;

    @BeforeEach
    void clear() throws Exception {
        authData.clearAllAuth();
        gameData.clearGames();
        userData.clearUsers();
        userJoe = new UserData("joe", "123", "joe@joe.com");
        auth = new AuthData("hey bro","22");
        game = new GameData(1,null,null,"wow",new ChessGame());
    }

    @Test
    void addAuth() throws Exception {
        var authorization = authData.addAuth(auth.username());
        assertEquals(authData.getAuth(authorization.authToken()).authToken(),authorization.authToken());
    }

    @Test
    void addAuthBadInput() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authData.addAuth(null);
        });
    }

    @Test
    void clearAll() throws Exception {
        assertEquals(0,authData.getAuthDatabase().size());
    }

    @Test
    void clearOne() throws Exception {
        var steve = authData.addAuth("steve");
        authData.clearAuth(steve.authToken());
        assertEquals(0,authData.getAuthDatabase().size());
    }

    @Test
    void clearOneThatDoesNotExist() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            authData.clearAuth(null);
        });
    }

    @Test
    void getAuth() throws Exception {
        var bob = authData.addAuth("bob");
        assertEquals(bob.username(),authData.getAuth(bob.authToken()).username());
        assertEquals(bob.authToken(),authData.getAuth(bob.authToken()).authToken());
    }

    @Test
    void getAuthUnauthorized() throws Exception {
        Assertions.assertThrows(UnauthorizedException.class, () -> {
            authData.getAuth("hdhdyee");
        });
    }

    @Test
    void getAuthDatatable() throws Exception {
        authData.addAuth("hey bro");
        assertEquals(1,authData.getAuthDatabase().size());
    }
    // how am I supposed to do a negative one? wtfreak

    @Test
    void createUser() throws Exception {
        var hey = userData.createUser("hey","hey","hey");
        assertEquals(hey.username(),userData.getUser("hey").username());
    }

    @Test
    void createUserBadRequest() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userData.createUser(null,null,null);
        });
    }

    @Test
    void getUser() throws Exception {
        var bro = userData.createUser("bro","bro","bro");
        assertEquals(bro.username(),userData.getUser("bro").username());
        assertEquals(bro.password(),userData.getUser("bro").password());
        assertEquals(bro.email(),userData.getUser("bro").email());
    }

    @Test
    void getUserBadRequest() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            userData.getUser(null);
        });
    }

    @Test
    void clearUsers() throws Exception {
        var joe = userData.getUser("joe");
        assertNull(joe);
    }

    @Test
    void createGame() throws Exception {
        var myGame = gameData.createGame(game);
        assertEquals(myGame,game);
    }

    @Test
    void createGameBadRequest() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameData.createGame(null);
        });
    }

    @Test
    void getGame() throws Exception {
        var myGame = gameData.createGame(game);
        assertEquals(myGame.gameID(),gameData.getGame(myGame.gameID()).gameID());
    }

    @Test
    void getGameNoSuchGame() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameData.getGame(1234567);
        });
    }

    @Test
    void listGame() throws Exception {
        assertEquals(0,gameData.listGames().size());
        var h = gameData.createGame(game);
        assertEquals(1,gameData.listGames().size());
    }

    @Test
    void updateGamePlayer() throws Exception {
        var h = gameData.createGame(game);
        gameData.updateGamePlayer(h, WHITE,"jimbob");
        assertEquals("jimbob",gameData.getGame(h.gameID()).whiteUsername());
    }

    @Test
    void updateGamePlayerAlreadyTaken() throws Exception {
        var h = gameData.createGame(game);
        gameData.updateGamePlayer(h, WHITE,"jimbob");
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameData.updateGamePlayer(gameData.getGame(h.gameID()),WHITE,"marysue");
        });
    }

    @Test
    void clearGames() throws Exception {
        assertEquals(0,gameData.listGames().size());
    }

    @Test
    void deleteGame() throws Exception {
        var h = gameData.createGame(game);
        gameData.deleteGame(h.gameID());
        assertEquals(0,gameData.listGames().size());
    }

    @Test
    void deleteGameBadRequest() throws Exception {
        Assertions.assertThrows(DataAccessException.class, () -> {
            gameData.deleteGame(-1);
        });
    }

}
