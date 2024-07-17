package dataaccess;

import model.UserData;

public interface UserDAO {
    public UserData createUser(String username, String password, String email);
    public UserData getUser(String username) throws DataAccessException;
    public void clearUsers();
}
