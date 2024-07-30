package dataaccess;

import exception.UnauthorizedException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class UserSQLDAO implements UserDAO {

    public UserSQLDAO() {
        configureDatabase();
    }

    private void configureDatabase() {
        String DATATABLE_NAME = "userDatatable";
        try (var conn = DatabaseManager.getConnection()) {
            String tableString = "CREATE table IF NOT EXISTS "+DATATABLE_NAME+" (username VARCHAR(128), password VARCHAR(128), email VARCHAR(128))";
            var preparedStatement = conn.prepareStatement(tableString);
            preparedStatement.executeUpdate(); // this should only happen if the table doesn't exist...
        } catch (SQLException | DataAccessException ex) {
            System.out.println("can't configure database, bro! Audaciously bogus!");
        }
    }


    @Override
    public UserData createUser(String username, String password, String email) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "INSERT INTO userDatatable (username, password, email) VALUES (?,?,?)";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,password);
            preparedStatement.setString(3,email);
            var id = preparedStatement.executeUpdate();
            return new UserData(username, password, email);
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with createUser");
        }
    }

    @Override
    public UserData getUser(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, password, email FROM userDatatable WHERE username = ?" ;
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,username);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                String password = "";
                String email = "";
                while (resultSet.next()) {
                    password = resultSet.getString("password");
                    email = resultSet.getString("email");
                }
                return new UserData(username,password,email);
            }
            return null;
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with getUser");
        }
    }

    @Override
    public void clearUsers() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE userDatatable";
            var preparedStatement = conn.prepareStatement(statement);
            var id = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with clearUsers");
        }
    }
}
