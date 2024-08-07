package client;

import websocket.messages.ErrorMessage;
import websocket.messages.ServerMessage;

public class ErrorHandler implements ServerMessageHandler{
    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getMessage());
    }

    public void notifyOfError(ErrorMessage errorMessage){
        System.out.println(errorMessage.errorMessage);
    }
}
