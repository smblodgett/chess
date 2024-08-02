package client;

import chess.ChessGame;
import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;

import static org.junit.jupiter.api.Assertions.*;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade facade;


    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @BeforeEach
    public void clearFacade(){
        facade.clearData();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void sampleTest() {
        assertTrue(true);
    }

    @Test
    void register() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        assertTrue(authData.authToken().length() > 10);
    }
// somehow, I'm not able to pass when every case goes. but individual cases pass just fine.

    @Test
    void registerTwice() throws Exception {
        var authData = facade.register("player1", "password", "p1@email.com");
        var authData2 = facade.register("player1", "password", "p2@email.com");
        var id1 = facade.createGame("game1",authData.authToken());
        assertThrows(NullPointerException.class,()->
                facade.createGame("sdsdfsdf", authData2.authToken()));
    }

    @Test
    void login() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        var authData2 = facade.login("player1","password");
        assertEquals(authData1.username(),authData2.username());
    }

    @Test
    void badLogin() throws Exception {
        var authData = facade.login("hey","sdfsdfsdfsdf");
        assertNull(authData);
    }

    @Test
    void listGames() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        var list = facade.listGames(authData1.authToken()).removeChessBoards();
        assertEquals(0,list.size());
    }

    @Test
    void listGamesBadAuth() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        assertThrows(NullPointerException.class,()->
                facade.listGames("sdsdfsdf").removeChessBoards());
//        assertEquals(0,list.size());
    }

    @Test
    void createGame() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        facade.createGame("testgame",authData1.authToken());
        assertEquals(1,facade.listGames(authData1.authToken()).removeChessBoards().size());
    }

    @Test
    void createGameGoneWrong() throws Exception {
        facade.createGame("testgame","yeeeee");
        var authData1 = facade.register("player1", "password", "p1@email.com");
        assertEquals(0,facade.listGames(authData1.authToken()).removeChessBoards().size());
    }

    @Test
    void joinGame() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        facade.createGame("testgame",authData1.authToken());
        facade.joinGame(ChessGame.TeamColor.WHITE,1,authData1.authToken());
        var list = facade.listGames(authData1.authToken()).removeChessBoards();
        assertEquals(list.get(0).whiteUsername(),"player1");
    }

    @Test
    void joinGameBadAuth() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        facade.createGame("testgame",authData1.authToken());
        facade.joinGame(ChessGame.TeamColor.WHITE,1,"1345");
        var list = facade.listGames(authData1.authToken()).removeChessBoards();
        assertEquals(list.get(0).whiteUsername(),null);
    }

    @Test
    void clearEverything() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        facade.createGame("testgame",authData1.authToken());
        facade.createGame("3",authData1.authToken());
        facade.createGame("testgasfssdffdsfdsme",authData1.authToken());
        facade.clearData();
        var newAuth = facade.register("me","ee","sdf");
        assertEquals(0,facade.listGames(newAuth.authToken()).removeChessBoards().size());
    }

}
