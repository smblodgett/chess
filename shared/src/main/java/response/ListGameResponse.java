package response;

import model.GameData;

import java.util.List;

public record ListGameResponse(List<GameData> games) {

}
