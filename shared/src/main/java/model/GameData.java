package model;

import chess.ChessGame;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

public record GameData(int gameID, String whiteUsername, String blackUsername, String gameName, ChessGame game,boolean isOver) {

    public JsonObject toJson() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.add("gameID", new JsonPrimitive(gameID()));
        jsonObject.add("whiteUsername", new JsonPrimitive(whiteUsername()));
        jsonObject.add("blackUsername", new JsonPrimitive(blackUsername()));
        jsonObject.add("gameName", new JsonPrimitive(gameName()));
        // Do not include ChessGame
        return jsonObject;
    }

}
