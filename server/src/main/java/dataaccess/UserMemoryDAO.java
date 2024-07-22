package dataaccess;

import model.UserData;

import java.util.HashSet;
import java.util.Objects;

public class UserMemoryDAO implements UserDAO {

    public static HashSet<UserData>  userDatabase  = new HashSet<>();

    public UserMemoryDAO(){
    }

    public UserData createUser(String username, String password, String email){
        userDatabase.add(new UserData(username, password,email));
        return new UserData(username, password,email);
    }

    public UserData getUser(String username) {
        for (var user : userDatabase){
            if (Objects.equals(user.username(), username)){
                return user;
            }
        }
        return null;

    }

    public void clearUsers(){
        userDatabase.clear();
    }
}
