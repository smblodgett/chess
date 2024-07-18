package dataaccess;

import model.AuthData;

import java.util.HashSet;
import java.util.Objects;

public class AuthDataAccess implements AuthDAO {
    HashSet<AuthData> authDatabase;

    public AuthDataAccess(){
        authDatabase= new HashSet<>();
    }

    @Override
    public AuthData addAuth(String authToken, String username){
        AuthData addedAuthData = new AuthData(authToken,username);
        authDatabase.add(addedAuthData);
        return addedAuthData;
    }

    @Override
    public void clearAuth(){
        authDatabase.clear();
    }

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (var auth : authDatabase){
            if (Objects.equals(auth.authToken(), authToken)){
                return auth;
            }
        }
        throw new DataAccessException("your auth token matches no username in the data files!");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthDataAccess that = (AuthDataAccess) o;
        return Objects.equals(authDatabase, that.authDatabase);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(authDatabase);
    }
}
