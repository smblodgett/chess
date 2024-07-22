package dataaccess;

public class DataAccessContainer {
    public AuthDAO authData;
    public GameDAO gameData;
    public UserDAO userData;


    public DataAccessContainer(AuthDAO authData, GameDAO gameData, UserDAO userData) {
        this.authData = authData;
        this.gameData = gameData;
        this.userData = userData;
    }
}
