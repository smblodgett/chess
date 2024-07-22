package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData addAuth(String username);
    public void clearAllAuth();
    public void clearAuth(String authToken);
    public AuthData getAuth(String authToken) throws DataAccessException;
}
