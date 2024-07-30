package dataaccess;

import com.google.gson.Gson;
import exception.DatabaseException;
import exception.ResponseException;
import exception.UnauthorizedException;
import model.AuthData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

import java.util.UUID;


public class AuthSQLDAO implements AuthDAO {

    public AuthSQLDAO()  {
        configureDatabase();
    }


    private void configureDatabase() {
        String DATATABLE_NAME = "authDatatable";
        try (var conn = DatabaseManager.getConnection()) {
            String tableString = "CREATE table IF NOT EXISTS "+DATATABLE_NAME+" (username VARCHAR(128), auth VARCHAR(128))";
            var preparedStatement = conn.prepareStatement(tableString);
            preparedStatement.executeUpdate(); // this should only happen if the table doesn't exist...
        } catch (SQLException | DataAccessException ex) {
            System.out.println("can't configure authDatatable");
        }
    }


    @Override
    public AuthData addAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var authToken = UUID.randomUUID().toString();
            var statement = "INSERT INTO authDatatable (username, auth) VALUES (?,?)";
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,username);
            preparedStatement.setString(2,authToken);
            var id = preparedStatement.executeUpdate();
            return new AuthData(username, authToken);
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with addAuth");
        }
    }

    @Override
    public void clearAllAuth() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE authDatatable";
            var preparedStatement = conn.prepareStatement(statement);
            var id = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with clearAll");
        }
    }

    @Override
    public void clearAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM authDatatable WHERE auth = ? " ;
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,authToken);
            var id = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with clearAuth");
        }
    }

    @Override
    public AuthData getAuth(String authToken) throws UnauthorizedException, DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username, auth FROM authDatatable WHERE auth = ?" ;
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setString(1,authToken);
            var resultSet = preparedStatement.executeQuery();
            String username = "";
            if (resultSet.isBeforeFirst()) {
                while (resultSet.next()) {
                    username = resultSet.getString("username");
                }
                return new AuthData(authToken, username);
            }
            else {throw new UnauthorizedException("Error: unauthorized");}
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with getAuth");
        }
    }

    @Override
    public HashSet<AuthData> getAuthDatabase() throws DataAccessException {

        HashSet<AuthData> authDatabase = new HashSet<>();

        String sql = "SELECT username, auth FROM authDatatable";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                String username = rs.getString(1);
                String authToken = rs.getString(2);
                authDatabase.add(new AuthData(username,authToken));
            }
            return authDatabase;
        }
        catch (SQLException ex) {
            throw new DataAccessException("yeHAW error with getAuthDatabase");
        }
    }
}
