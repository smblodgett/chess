import chess.*;
import client.UserOI;

import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        UserOI clientActions = new UserOI();
        System.out.println(SET_BG_COLOR_LIGHT_GREY+SET_TEXT_COLOR_BLUE+"♕ Welcome to 240 Chess. Type help to get started. ♕"+RESET_TEXT_COLOR);
        clientActions.run();
    }

}