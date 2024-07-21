package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class AuthMemoryDAO implements AuthDAO {
    static HashSet<AuthData> authDatabase;

    public AuthMemoryDAO() {
        authDatabase = new HashSet<>();
    }

    @Override
    public AuthData addAuth(String username) {
        AuthData addedAuthData = new AuthData(UUID.randomUUID().toString(), username);
        authDatabase.add(addedAuthData);
        return addedAuthData;
    }

    @Override
    public void clearAuth() {
        authDatabase.clear();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (var auth : authDatabase) {
            if (Objects.equals(auth.authToken(), authToken)) {
                return auth;
            }
        }
        throw new DataAccessException("your auth token matches no username in the data files!");
    }
}