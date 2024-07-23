package dataaccess;

import exception.UnauthorizedException;
import model.AuthData;

import java.util.HashSet;

public interface AuthDAO {
    public AuthData addAuth(String username);
    public void clearAllAuth();
    public void clearAuth(String authToken);
    public AuthData getAuth(String authToken) throws UnauthorizedException;
    public HashSet<AuthData> getAuthDatabase();
}
