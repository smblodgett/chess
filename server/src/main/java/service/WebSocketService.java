package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import model.GameData;
import websocket.commands.ConnectCommand;
import websocket.commands.MakeMoveCommand;
import websocket.commands.UserGameCommand;

public class WebSocketService {

    private static DataAccessContainer data;

    WebSocketService(DataAccessContainer dataAccessContainer) {
        data=dataAccessContainer;
    }

    public void connect(ConnectCommand command){
        int gameID = command.getGameID();
        try {
            GameData game = data.gameData.getGame(gameID);
        }
        catch (DataAccessException ex) { // perhaps needs to print if there isn't a game of that id
            System.out.println(ex.getMessage());
        }
        data.gameData.updateGamePlayer(, );
    }

    public void makeMove(MakeMoveCommand command){

    }

    public void leaveGame(UserGameCommand command) {

    }

    public void resignGame(UserGameCommand command) {

    }
}
