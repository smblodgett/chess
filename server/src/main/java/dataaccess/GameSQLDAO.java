package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.AuthData;
import model.GameData;
import model.UserData;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;

public class GameSQLDAO implements GameDAO {

    public GameSQLDAO() {
        String DATATABLE_NAME = "gameDatatable";
        try (var conn = DatabaseManager.getConnection()) {
            String tableString = "CREATE table IF NOT EXISTS "+DATATABLE_NAME+" (gameID INT NOT NULL AUTO_INCREMENT PRIMARY KEY, whiteUsername VARCHAR(128), blackUsername VARCHAR(128), gameName VARCHAR(128), chessGame VARCHAR(8192))";
            var preparedStatement = conn.prepareStatement(tableString);
            preparedStatement.executeUpdate(); // this should only happen if the table doesn't exist...
        } catch (SQLException | DataAccessException ex) {
            System.out.println("can't configure gameDatatable!");
        }
    }

    @Override
    public GameData createGame(GameData gameData) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            if (gameData==null){throw new DataAccessException("passed in null");}
            var statement = "INSERT INTO gameDatatable (whiteUsername, blackUsername, gameName, chessGame) VALUES (?,?,?,?)";
            var preparedStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1,gameData.whiteUsername());
            preparedStatement.setString(2,gameData.blackUsername());
            preparedStatement.setString(3,gameData.gameName());
            String gameAsString = new Gson().toJson(gameData.game());
            preparedStatement.setString(4,gameAsString);
            var id = preparedStatement.executeUpdate();
            return gameData;
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with createGame");
        }
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM gameDatatable WHERE gameID = ?" ;
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,gameID);
            var resultSet = preparedStatement.executeQuery();
            if (resultSet.isBeforeFirst()) {
                String whiteUsername = "";
                String blackUsername = "";
                String gameName = "";
                String gameAsString = "";
                while (resultSet.next()) {
                    whiteUsername = resultSet.getString("whiteUsername");
                    blackUsername = resultSet.getString("blackUsername");
                    gameName = resultSet.getString("gameName");
                    gameAsString = resultSet.getString("chessGame");
                }
                ChessGame game = new Gson().fromJson(gameAsString, ChessGame.class);
                return new GameData(gameID,whiteUsername,blackUsername,gameName,game);
            }
            throw new DataAccessException("no such game");
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with getUser");
        }
    }

    @Override
    public ArrayList<GameData> listGames() throws DataAccessException {

        ArrayList<GameData> gameDataList = new ArrayList<>();

        String sql = "SELECT gameID, whiteUsername, blackUsername, gameName, chessGame FROM gameDatatable";

        try(PreparedStatement stmt = DatabaseManager.getConnection().prepareStatement(sql);
            ResultSet rs = stmt.executeQuery()) {

            while(rs.next()) {
                int gameID = rs.getInt("gameID");
                String whiteUsername = rs.getString("whiteUsername");
                String blackUsername = rs.getString("blackUsername");
                String gameName = rs.getString("gameName");
                String gameAsString = rs.getString("chessGame");
                ChessGame game = new Gson().fromJson(gameAsString, ChessGame.class);
                gameDataList.add(new GameData(gameID, whiteUsername, blackUsername, gameName, game));
            }
            return gameDataList;
        }
        catch (SQLException ex) {
            throw new DataAccessException("yeHAW error with getAuthDatabase");
        }
    }

    @Override
    public void updateGamePlayer(GameData gameData, ChessGame.TeamColor color, String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            int gameID = gameData.gameID();

            String blackName = gameData.blackUsername();
            String whiteName = gameData.whiteUsername();

            if (color == ChessGame.TeamColor.WHITE){
                if (whiteName!=null) {throw new DataAccessException("someone else is already white!");}
                var statement = "UPDATE gameDatatable SET whiteUsername = ? WHERE gameID = ?";
                var preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setInt(2,gameID);
                preparedStatement.setString(1,username);
                var id = preparedStatement.executeUpdate();
            }
            if (color == ChessGame.TeamColor.BLACK){
                if (blackName!=null) {throw new DataAccessException("someone else is already black!");}
                var statement = "UPDATE gameDatatable SET blackUsername = ? WHERE gameID = ?";
                var preparedStatement = conn.prepareStatement(statement);
                preparedStatement.setString(1,username);
                preparedStatement.setInt(2,gameID);
                var id = preparedStatement.executeUpdate();
            }
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with updateGamePlayer");
        }
    }

    @Override
    public void clearGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "TRUNCATE gameDatatable";
            var preparedStatement = conn.prepareStatement(statement);
            var id = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with clearGames");
        }
    }

    @Override
    public void deleteGame(int gameID) throws DataAccessException {
        if (gameID<0) {throw new DataAccessException("invalid gameID");}
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "DELETE FROM gameDatatable WHERE gameID = ? " ;
            var preparedStatement = conn.prepareStatement(statement);
            preparedStatement.setInt(1,gameID);
            var id = preparedStatement.executeUpdate();
        }
        catch (SQLException ex) {
            throw new DataAccessException("error with clearAuth");
        }
    }
}
