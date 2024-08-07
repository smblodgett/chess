package client;

import chess.ChessGame;
import com.google.gson.Gson;
import ui.ChessBoardPrinter;
import websocket.messages.LoadGameMessage;
import websocket.messages.ServerMessage;

public class LoadGameMessageHandler implements ServerMessageHandler {
    @Override
    public void notify(ServerMessage serverMessage) {
        System.out.println(serverMessage.getMessage());
    }

    public void updateGame(LoadGameMessage gameMessage) {
        UserOI.currentGame=gameMessage.getChessGame();
    }

    public void printNewGame(LoadGameMessage loadGameMessage, ChessGame.TeamColor colorToPrintSideOf) {
        ChessGame game = loadGameMessage.getChessGame();
        ChessBoardPrinter printer = new ChessBoardPrinter(game,colorToPrintSideOf);
        printer.drawEverything();
    }
}
