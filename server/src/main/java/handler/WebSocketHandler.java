package handler;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import server.WebSocketSessionsManager;
import service.WebSocketService;

@WebSocket
public class WebSocketHandler {

    private WebSocketService service;
    private WebSocketSessionsManager sessionsManager;

    public WebSocketHandler(WebSocketService webSocketService, WebSocketSessionsManager manager){
        this.service=webSocketService;
        this.sessionsManager=manager;
    }

    @OnWebSocketConnect
    public void onConnect(Session session){

    }





}
