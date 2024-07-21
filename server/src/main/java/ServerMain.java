import dataaccess.AuthMemoryDAO;
import dataaccess.DataAccessContainer;
import dataaccess.GameMemoryDAO;
import dataaccess.UserMemoryDAO;
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
//            var authData = new AuthMemoryDAO();
//            var gameData = new GameMemoryDAO();
//            var userData = new UserMemoryDAO();
//            var dataContainer = new DataAccessContainer(authData,gameData,userData);
            var services = new ServiceContainer(dataContainer); // make handler/service reference data statically
            var server = new Server();
            server.addServices(services);
            server.run(port);
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
