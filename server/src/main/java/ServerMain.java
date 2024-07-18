import dataaccess.AuthDataAccess;
import dataaccess.DataAccessContainer;
import dataaccess.GameDataAccess;
import dataaccess.UserDataAccess;
import service.ServiceContainer;
import server.Server;

public class ServerMain {
    /**
     * Starts the server on the given port. If port is 0 then a random port is used.
     */
    public static void main(String[] args) {
        try {
            var port = 8080;
            if (args.length >= 1) {
                port = Integer.parseInt(args[0]);
            }
            var authData = new AuthDataAccess();
            var gameData = new GameDataAccess();
            var userData = new UserDataAccess();
            var dataContainer = new DataAccessContainer(authData,gameData,userData);
            var services = new ServiceContainer(dataContainer);
            var server = new Server(services).run(port);
            port = server.port();
            System.out.printf("Server started on port %d%n", port);
            return;
        } catch (Throwable ex) {
            System.out.printf("Unable to start server: %s%n", ex.getMessage());
        }
        System.out.println("""
                Chess Server:
                java ServerMain <port> [<dburl> <dbuser> <dbpassword> <dbname>]
                """);
    }
}
