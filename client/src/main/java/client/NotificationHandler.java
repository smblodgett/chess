package client;

import websocket.messages.ServerMessage;

public class NotificationHandler implements ServerMessageHandler{
    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getMessage());
    }
}
