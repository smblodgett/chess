package websocket.commands;

import chess.ChessMove;

public class MakeMoveCommand extends UserGameCommand{

    public MakeMoveCommand(CommandType commandType, String authToken, Integer gameID,ChessMove move) {
        super(commandType, authToken, gameID,move);
    }

    public ChessMove getChessMove() {
        return move;
    }

}
