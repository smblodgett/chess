package service;

import dataaccess.DataAccessContainer;
import dataaccess.DataAccessException;
import exception.BadRequestException;
import exception.UnauthorizedException;
import model.AuthData;
import model.GameData;

import java.util.Random;

public class CreateGameService {

    private DataAccessContainer data;

    public CreateGameService(DataAccessContainer dataAccessContainer){
        this.data=dataAccessContainer;
    }

    public void createNewGame(GameData game) throws BadRequestException, DataAccessException {
        if (game.gameName()==null) {throw new BadRequestException("Error: bad request");}
        data.gameData.createGame(game);
    }

    public int makeGameID(){
        Random random = new Random();

        // Generate a random 8-digit number
        int min = 10000000; // Minimum 8-digit number
        int max = 99999999; // Maximum 8-digit number

        return random.nextInt(max - min + 1) + min;
    }

    public boolean checkUniqueID(int gameID){
        try {
            GameData thisGame = data.gameData.getGame(gameID);
            return false;
        }
        catch (DataAccessException e) {
            return true;
        }
    }

    public AuthData authenticate(String authToken) throws UnauthorizedException, DataAccessException {
        if (data.authData.getAuth(authToken)==null){
            throw new UnauthorizedException("Error: unauthorized");
        }
        return data.authData.getAuth(authToken);
    }


}
