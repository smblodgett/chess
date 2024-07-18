package dataaccess;

public class DataAccessContainer {
    public final AuthDAO authData;
    public final GameDAO gameData;
    public final UserDAO userData;


    public DataAccessContainer(AuthDAO authData, GameDAO gameData, UserDAO userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
}
