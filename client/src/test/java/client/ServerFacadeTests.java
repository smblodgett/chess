package client;

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

    @Test
    void login() throws Exception {
        var authData1 = facade.register("player1", "password", "p1@email.com");
        var authData2 = facade.login("player1","password");
        assertEquals(authData1,authData2);
    }

    @Test
    void badLogin() throws Exception {
        var authData = facade.login("hey","sdfsdfsdfsdf");
        assertNull(authData);
    }



}
