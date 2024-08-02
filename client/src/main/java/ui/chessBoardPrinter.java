package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class chessBoardPrinter {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static PrintStream out;
    private ChessGame chessGame;
    private static boolean isWhiteToPrint=true;

    public chessBoardPrinter(ChessGame chessGame) {
        this.chessGame = chessGame;
        out = new PrintStream(System.out,true, StandardCharsets.UTF_8);
    }

    public void drawEverything(){
        System.out.println("beginning printing :0");

        String headerText1 = SET_BG_COLOR_LIGHT_GREY+SET_TEXT_COLOR_BLACK+EMPTY+
                " H"+EMPTY+"G"+EMPTY+"F"+EMPTY+"E"+EMPTY+"D"+EMPTY+"C"+EMPTY+"B"+EMPTY+"A"+EMPTY+"\n";

        String headerText2 = SET_BG_COLOR_LIGHT_GREY+SET_TEXT_COLOR_BLACK+EMPTY+
                " A"+EMPTY+"B"+EMPTY+"C"+EMPTY+"D"+EMPTY+"E"+EMPTY+"F"+EMPTY+"G"+EMPTY+"H"+EMPTY+"\n";

        drawHeader(headerText1);
        drawBoard(chessGame, new int[]{1,2,3,4,5,6,7,8});
        drawHeader(headerText1);
        out.print("\n");
        drawHeader(headerText2);
        drawBoard(chessGame, new int[]{8, 7, 6, 5, 4, 3, 2, 1});
        drawHeader(headerText2);
    }

    private void drawHeader(String headerText){
        out.print(headerText);
    }

    private void drawBoard(ChessGame chessGame,int[] order){
        ChessPiece[][] pieceGrid = chessGame.getBoard().getPieceGrid();
        for (var rowNumber : order){
            ChessPiece[] row = pieceGrid[rowNumber];
            out.print(SET_BG_COLOR_LIGHT_GREY +" "+rowNumber+EMPTY);
            for (var colNumber : order){
                ChessPiece piece = row[colNumber];
                String pieceText = handlePiecePrint(piece);
                out.print(getSquareColorBackground()+pieceText);
            }
            isWhiteToPrint= !isWhiteToPrint;
            out.print(SET_BG_COLOR_LIGHT_GREY+EMPTY+rowNumber+"\n");
        }
        isWhiteToPrint=true;
    }

    private String handlePiecePrint(ChessPiece piece){
        if (piece==null) {return EMPTY;}
        var color = piece.getTeamColor();
        var type = piece.getPieceType();
        if (color== ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case ROOK -> WHITE_ROOK;
                case KNIGHT -> WHITE_KNIGHT;
                case BISHOP -> WHITE_BISHOP;
                case QUEEN -> WHITE_QUEEN;
                case KING -> WHITE_KING;
                case PAWN -> WHITE_PAWN;
            };
        }
        else if (color == ChessGame.TeamColor.BLACK){
            return switch(type){
                case ROOK -> BLACK_ROOK;
                case KNIGHT -> BLACK_KNIGHT;
                case BISHOP -> BLACK_BISHOP;
                case QUEEN -> BLACK_QUEEN;
                case KING -> BLACK_KING;
                case PAWN -> BLACK_PAWN;
            };
        }
        else {return EMPTY;}
    }

    private String getSquareColorBackground(){
        if (isWhiteToPrint){
            isWhiteToPrint=false;
            return SET_BG_COLOR_WHITE;
        }
        else {
            isWhiteToPrint=true;
            return SET_BG_COLOR_BLACK;
        }
    }
}
