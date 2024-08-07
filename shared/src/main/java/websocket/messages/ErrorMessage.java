package websocket.messages;

public class ErrorMessage extends ServerMessage{

    String errorMessage;
    public ErrorMessage(ServerMessageType type, String errorMessage) {
        super(type,null);
        this.errorMessage=errorMessage;
    }
}
