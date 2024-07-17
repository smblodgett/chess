package dataaccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData addAuth(String authToken, String username);
    public void clearAuth();
    public AuthData getAuth(String authToken) throws DataAccessException;
}
