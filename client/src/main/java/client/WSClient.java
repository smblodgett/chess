package client;

import com.google.gson.Gson;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;


import javax.websocket.*;
import java.net.URI;
import java.util.Scanner;

public class WSClient extends Endpoint {

    public static void main(String[] args) throws Exception {
        var ws = new WSClient();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter a message you want to echo");
        while (true) {
            ws.send(scanner.nextLine());
        }
    }


    public Session session;

    public WSClient() throws Exception {
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            public void onMessage(String message) {
                System.out.println(message);
                Gson gson = new Gson();
                var serverMessage = gson.fromJson(message, ServerMessage.class);
                switch(serverMessage.getServerMessageType()){
                    case LOAD_GAME:
                        handleLoadGame(new Gson().fromJson(message,LoadGameMessage.class));
                        break;
                    case ERROR:
                        handleError(new Gson().fromJson(message,ErrorMessage.class));
                        break;
                    case NOTIFICATION:
                        handleNotification(new Gson().fromJson(message,NotificationMessage.class));
                        break;
                    default:
                }
            }
        });
    }

    private void handleLoadGame(LoadGameMessage message){
        UserOI.currentGame = message.getChessGame();
    }

    private void handleError(ErrorMessage message){
        System.out.println(message.getMessage());
    }

    private void handleNotification(NotificationMessage message){
        System.out.println(message.getMessage());
    }

    public void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }
}
