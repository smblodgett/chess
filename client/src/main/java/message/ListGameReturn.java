package message;

import model.GameData;

import java.util.ArrayList;
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
