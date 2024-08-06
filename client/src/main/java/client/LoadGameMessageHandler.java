package client;

import com.google.gson.Gson;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class LoadGameMessageHandler implements ServerMessageHandler {
    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getMessage());
    }

    public void updateGame(LoadGameMessage gameMessage) {
        UserOI.currentGame=gameMessage.getChessGame();
    }
}
