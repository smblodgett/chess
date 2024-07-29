package dataaccess;

import exception.UnauthorizedException;
import model.AuthData;
import model.UserData;

import java.sql.SQLException;
import java.util.UUID;

public class UserSQLDAO implements UserDAO {

    public UserSQLDAO() throws DataAccessException {
        configureDatabase();
    }

    private void configureDatabase() throws DataAccessException {
        String DATABASE_NAME = "chessDatabase";
        String DATATABLE_NAME = "userDatatable";
        String checkDatabaseSQL = "SELECT SCHEMA_NAME FROM information_schema.SCHEMATA WHERE SCHEMA_NAME = ?";

        try (var conn = DatabaseManager.getConnection();
             var preparedStatement = conn.prepareStatement(checkDatabaseSQL)) {
            preparedStatement.setString(1,DATABASE_NAME);
            if (!preparedStatement.executeQuery().isBeforeFirst()){
                DatabaseManager.createDatabase(); // should just try and login if database already exists
            }

        } catch (SQLException ex) {
            throw new DataAccessException("can't configure database, bro! Audaciously bogus!");
        }

        try (var conn = DatabaseManager.getConnection()) {
            String tableString = "CREATE table IF NOT EXISTS "+DATATABLE_NAME+" (username VARCHAR(128), password VARCHAR(128), email VARCHAR(128))";
            var preparedStatement = conn.prepareStatement(tableString);
            preparedStatement.executeUpdate(); // this should only happen if the table doesn't exist...
        } catch (SQLException ex) {
            throw new DataAccessException("can't configure database, bro! Audaciously bogus!");
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
            var statement = "DROP TABLE IF EXISTS userDatatable";
            var preparedStatement = conn.prepareStatement(statement);
            var id = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with clearUsers");
        }
    }
}
