package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class UserDataAccess implements UserDAO {

    HashSet<UserData> userDatabase;

    public UserDataAccess(){
        userDatabase = new HashSet<>();
    }

    public UserData createUser(String username, String password, String email){
        return new UserData(username, password,email);
    }

    public UserData getUser(String username) throws DataAccessException {
        for (var user : userDatabase){
            if (Objects.equals(user.username(), username)){
                return user;
            }
        }
        throw new DataAccessException("sorry, that user doesn't exist in the database...");

    }

    public void clearUsers(){
        userDatabase.clear();
    }
}
