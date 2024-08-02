import chess.*;
import client.UserOI;

import static client.UserOI.DIVIDERS;
import static ui.EscapeSequences.*;

public class Main {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        UserOI clientActions = new UserOI();
        System.out.println(SET_BG_COLOR_LIGHT_GREY+DIVIDERS);
        System.out.println(DIVIDERS);
        System.out.println(SET_TEXT_COLOR_BLUE+"♙♞♔♝♕ Welcome to 240 Chess.\u2006Type help to get started. ♕♝♔♞♙"+RESET_TEXT_COLOR);
        System.out.println(DIVIDERS);
        System.out.println(DIVIDERS);
        clientActions.run();
    }

}