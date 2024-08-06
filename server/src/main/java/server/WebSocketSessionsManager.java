package server;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

public class WebSocketSessionsManager {
    public final ConcurrentHashMap<Integer, Set<Session>> connections = new ConcurrentHashMap<>();

    public void addSessionToGame(int gameID, Session session) {
        var connection = new Connection(gameID, session);
        if (connections.get(gameID).isEmpty()){
            Set<Session> setToInsert = new HashSet<>();
            setToInsert.add(session);
            connections.put(gameID,setToInsert);
        }
        else {
            Set<Session> setToInsert = connections.get(gameID);
            setToInsert.add(session);
            connections.put(gameID, setToInsert);
        }
    }

    public void remove(String visitorName) {
        connections.remove(visitorName);
    }

    public ConcurrentHashMap<Integer, Set<Session>> getAllSessions() {
        return connections;
    }


}
