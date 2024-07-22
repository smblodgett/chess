package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

public class AuthMemoryDAO implements AuthDAO {
    public static HashSet<AuthData> authDatabase  = new HashSet<>();

    public AuthMemoryDAO() {
    }

    @Override
    public AuthData addAuth(String username) {
        AuthData addedAuthData = new AuthData(username, UUID.randomUUID().toString());
        authDatabase.add(addedAuthData);
        return addedAuthData;
    }

    @Override
    public void clearAllAuth() {
        authDatabase.clear();
    }

    public void clearAuth(String authToken){
        authDatabase.removeIf(auth -> Objects.equals(auth.authToken(), authToken));
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