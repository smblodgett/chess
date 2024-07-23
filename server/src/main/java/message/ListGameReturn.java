package message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.GameData;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public record ListGameReturn(ArrayList<GameData> games) {

    public List<GameData> removeChessBoards() {
        List<GameData> gamesToReturn = new ArrayList<>();
        for (GameData gameData : games){
            gamesToReturn.add(new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(),null));
        }
        return gamesToReturn;
    }

}
