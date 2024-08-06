package websocket.commands;

import chess.ChessMove;

public class LeaveCommand extends UserGameCommand{

    public String username;

    public LeaveCommand(CommandType commandType, String authToken, Integer gameID, ChessMove move, String username) {
        super(commandType, authToken, gameID, move);
        this.username=username;
    }
}
