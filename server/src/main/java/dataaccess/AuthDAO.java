package dataaccess;

import exception.ResponseException;
import exception.UnauthorizedException;
import model.AuthData;

import java.sql.SQLException;
import java.util.HashSet;

public interface AuthDAO {
    public AuthData addAuth(String username) throws DataAccessException;
    public void clearAllAuth() throws DataAccessException;
    public void clearAuth(String authToken) throws DataAccessException;
    public AuthData getAuth(String authToken) throws UnauthorizedException, DataAccessException;
    public HashSet<AuthData> getAuthDatabase() throws DataAccessException;
}
