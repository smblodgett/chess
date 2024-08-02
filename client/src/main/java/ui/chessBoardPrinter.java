package ui;

import chess.ChessGame;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class chessBoardPrinter {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static PrintStream out;
    private ChessGame chessGame;

    public chessBoardPrinter(ChessGame chessGame) {
        this.chessGame = chessGame;
        out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
    }

    public void drawEverything(){
        String headerText = SET_BG_COLOR_LIGHT_GREY+SET_TEXT_COLOR_BLACK+EMPTY+
                "H"+EMPTY+"G"+EMPTY+"F"+EMPTY+"E"+EMPTY+"D"+EMPTY+"C"+EMPTY+"B"+"A";
        drawHeader(headerText);
        drawBoard(chessGame);
    }

    private void drawHeader(String headerText){
        out.print(headerText);
    }

    private void drawBoard(ChessGame chessGame){

        for (var row : chessGame.getBoard().getPieceGrid()){
            for (var piece: row){

            }
        }
    }
}
