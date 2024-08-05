package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Set;

import static ui.EscapeSequences.*;

public class ChessBoardPrinter {

    private static final int BOARD_SIZE_IN_SQUARES = 8;
    private static PrintStream out;
    private ChessGame chessGame;
    private static boolean isWhiteToPrint = true;
    private ChessGame.TeamColor playerColor;

    public ChessBoardPrinter(ChessGame chessGame, ChessGame.TeamColor playerColor) {
        this.chessGame = chessGame;
        this.playerColor = playerColor;
        out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
    }

    public void drawEverything() {

        String headerText1 = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + BUFFER +
                "   H" + "  " + "G" + " \u2006\u2006\u2006" + "F" + "  " + "E" + " \u2006\u2006\u2006" + "D" + "  "
                + "C" + " \u2006\u2006\u2006\u2006" + "B" + " \u2006\u2006\u2008" + "A" + "  " + "\n";

        String headerText2 = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + BUFFER +
                "   A" + "  " + "B" + " \u2006\u2006\u2006" + "C" + "  " + "D" + " \u2006\u2006\u2006" + "E" + "  "
                + "F" + " \u2006\u2006\u2006\u2006" + "G" + " \u2006\u2006\u2008" + "H" + "  " + "\n";
        if (playerColor == ChessGame.TeamColor.WHITE) {
            drawHeader(headerText1);
            drawBoard(chessGame, new int[]{1, 2, 3, 4, 5, 6, 7, 8});
            drawHeader(headerText1);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            drawHeader(headerText2);
            drawBoard(chessGame, new int[]{8, 7, 6, 5, 4, 3, 2, 1});
            drawHeader(headerText2);
        }
    }

    private void drawHeader(String headerText) {
        out.print(headerText);
    }

    private void drawBoard(ChessGame chessGame, int[] order) {
        ChessPiece[][] pieceGrid = chessGame.getBoard().getPieceGrid();
        for (var rowNumber : order) {
            ChessPiece[] row = pieceGrid[rowNumber];
            out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + rowNumber + BUFFER);
            for (var colNumber : orderReverser(order)) {
                ChessPiece piece = row[colNumber];
                String pieceText = handlePiecePrint(piece);
                out.print(getSquareColorBackground() + pieceText);
            }
            isWhiteToPrint = !isWhiteToPrint;
            out.print(SET_BG_COLOR_LIGHT_GREY + BUFFER + rowNumber + "\n" + RESET_TEXT_COLOR);
        }
        isWhiteToPrint = true;
    }

    private int[] orderReverser(int[] order) {
        int len = order.length;
        var reversed = new int[order.length];
        for (var item : order) {
            reversed[len - 1] = item;
            len--;
        }
        return reversed;
    }

    private String handlePiecePrint(ChessPiece piece) {
        if (piece == null) {
            return EMPTY;
        }
        var color = piece.getTeamColor();
        var type = piece.getPieceType();
        if (color == ChessGame.TeamColor.WHITE) {
            return switch (type) {
                case ROOK -> WHITE_ROOK;
                case KNIGHT -> WHITE_KNIGHT;
                case BISHOP -> WHITE_BISHOP;
                case QUEEN -> WHITE_QUEEN;
                case KING -> WHITE_KING;
                case PAWN -> WHITE_PAWN;
            };
        } else if (color == ChessGame.TeamColor.BLACK) {
            return switch (type) {
                case ROOK -> BLACK_ROOK;
                case KNIGHT -> BLACK_KNIGHT;
                case BISHOP -> BLACK_BISHOP;
                case QUEEN -> BLACK_QUEEN;
                case KING -> BLACK_KING;
                case PAWN -> BLACK_PAWN;
            };
        } else {
            return EMPTY;
        }
    }

    private String getSquareColorBackground() {
        if (isWhiteToPrint) {
            isWhiteToPrint = false;
            return SET_BG_COLOR_LIGHTER_GREY;
        } else {
            isWhiteToPrint = true;
            return SET_BG_COLOR_DARK_GREEN;
        }
    }

    public void highlightedBoard(ChessPosition positionToHighlight) {
//        System.out.println("beginning printing :0");

        String headerText1 = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + BUFFER +
                "   H" + "  " + "G" + " \u2006\u2006\u2006" + "F" + "  " + "E" + " \u2006\u2006\u2006" + "D" + "  "
                + "C" + " \u2006\u2006\u2006\u2006" + "B" + " \u2006\u2006\u2008" + "A" + "  " + "\n";

        String headerText2 = SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + BUFFER +
                "   A" + "  " + "B" + " \u2006\u2006\u2006" + "C" + "  " + "D" + " \u2006\u2006\u2006" + "E" + "  "
                + "F" + " \u2006\u2006\u2006\u2006" + "G" + " \u2006\u2006\u2008" + "H" + "  " + "\n";
        if (playerColor == ChessGame.TeamColor.WHITE) {
            drawHeader(headerText1);
            drawHighlightedBoard(chessGame, new int[]{1, 2, 3, 4, 5, 6, 7, 8}, positionToHighlight);
            drawHeader(headerText1);
        } else if (playerColor == ChessGame.TeamColor.BLACK) {
            drawHeader(headerText2);
            drawHighlightedBoard(chessGame, new int[]{8, 7, 6, 5, 4, 3, 2, 1}, positionToHighlight);
            drawHeader(headerText2);
        }
    }

    public void drawHighlightedBoard(ChessGame chessGame, int[] order, ChessPosition positionToHighlight) {
        ChessPiece[][] pieceGrid = chessGame.getBoard().getPieceGrid();
        for (var rowNumber : order) {
            ChessPiece[] row = pieceGrid[rowNumber];
            out.print(SET_BG_COLOR_LIGHT_GREY + SET_TEXT_COLOR_BLACK + " " + rowNumber + BUFFER);
            for (var colNumber : orderReverser(order)) {
                ChessPiece piece = row[colNumber];
                String pieceText = handlePiecePrint(piece);
                ChessPosition currentPosition = new ChessPosition(rowNumber, colNumber);
                out.print(getSquareColorBackgroundHighlighted(positionToHighlight, currentPosition) + pieceText);
            }
            isWhiteToPrint = !isWhiteToPrint;
            out.print(SET_BG_COLOR_LIGHT_GREY + BUFFER + rowNumber + "\n" + RESET_TEXT_COLOR);
        }
        isWhiteToPrint = true;
    }


    public String getSquareColorBackgroundHighlighted(ChessPosition positionToHighlight, ChessPosition currentPosition) {
        ChessBoard board = chessGame.getBoard();
        ChessPiece highlightedPiece = board.getPiece(positionToHighlight);
        if (positionToHighlight == currentPosition) {
            return SET_BG_COLOR_YELLOW;
        }
        Set<ChessMove> moves = (Set<ChessMove>) highlightedPiece.pieceMoves(board, positionToHighlight);
        for (ChessMove move : moves) {
            if (move.getEndPosition() == currentPosition && isWhiteToPrint) {
                isWhiteToPrint = false;
                return SET_BG_COLOR_LIGHT_GREEN;
            } else if (move.getEndPosition() == currentPosition && !isWhiteToPrint) {
                isWhiteToPrint = true;
                return SET_BG_COLOR_SKY_BLUE;
            }
        }
        if (isWhiteToPrint) {
            isWhiteToPrint = false;
            return SET_BG_COLOR_LIGHTER_GREY;
        } else {
            isWhiteToPrint = true;
            return SET_BG_COLOR_DARK_GREEN;
        }
    }
}
