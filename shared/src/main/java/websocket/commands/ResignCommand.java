package websocket.commands;

import chess.ChessMove;

public class ResignCommand extends UserGameCommand {

    public String username;

    public ResignCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move, String username) {
        super(commandType, authToken, gameID, move);
        this.username = username;
    }
}
