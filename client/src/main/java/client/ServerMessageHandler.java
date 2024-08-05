package client;
import websocket.messages.*;

public interface ServerMessageHandler {
    void notify(ServerMessage serverMessage);
}

