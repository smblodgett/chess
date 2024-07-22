package message;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.GameData;

import java.util.HashSet;

public record ListGameReturn(HashSet<GameData> games) {

    public String toJson() {
        Gson gson = new Gson();
        JsonObject jsonObject = new JsonObject();
        JsonArray jsonArray = new JsonArray();

        for (GameData gameData : games) {
            jsonArray.add(gameData.toJson());
        }

        jsonObject.add("games", jsonArray);
        return jsonObject.toString();
    }

}
