package chess;

import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {
    private ChessPiece[][] board = new ChessPiece[9][9];

    public ChessBoard() {
    }

    public ChessBoard(ChessBoard copy) {
        for (int i = 0; i < board.length; i++) {
            board[i] = new ChessPiece[copy.board[i].length];
            System.arraycopy(copy.board[i], 0, board[i], 0, board[i].length);
        }
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        if (position.getColumn() > 8 || position.getRow() > 8 || position.getColumn() < 0 || position.getRow() < 0){
            // print some kind of error saying you can't place there!
        }
        board[position.getRow()][position.getColumn()] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return board[position.getRow()][position.getColumn()];
    }

    public boolean isEnemyPiece(ChessPosition position, ChessGame.TeamColor myColor){
        ChessPiece piece = getPiece(position);
        return piece != null && (piece.getTeamColor() != myColor);
    }
    /**
     * tells if a piece is at a certain position
     *
     * @param position The position to get the piece from
     * @return True if a piece isn't there, false if there is
     */
    public boolean noPiece(ChessPosition position){
        return getPiece(position)==null;
    }

    /**
     * returns the chess piece grid
     *
     * @return variable board, the 8x8 chess piece array
     */
    public ChessPiece[][] getPieceGrid(){
        return board;
    }

    public void updateBoard(ChessPiece[][] newGrid){
        board = newGrid;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[9][9];
        for (int i=1;i<9;i++){
            // add pawns
            addPiece(new ChessPosition(2,i),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN));
            addPiece(new ChessPosition(7,i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.PAWN));
            // add rooks
            if (i==1||i==8){
                addPiece(new ChessPosition(1,i),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.ROOK));
                addPiece(new ChessPosition(8,i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.ROOK));
            }
            // add knights
            if (i==2||i==7){
                addPiece(new ChessPosition(1,i),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KNIGHT));
                addPiece(new ChessPosition(8,i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KNIGHT));
            }
            // add bishops
            if (i==3||i==6){
                addPiece(new ChessPosition(1,i),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.BISHOP));
                addPiece(new ChessPosition(8,i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.BISHOP));
            }
            // add queens
            if (i==4) {
                addPiece(new ChessPosition(1, i), new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.QUEEN));
                addPiece(new ChessPosition(8, i), new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.QUEEN));
            }
            // add kings
            if (i==5){
                addPiece(new ChessPosition(1,i),new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.KING));
                addPiece(new ChessPosition(8,i),new ChessPiece(ChessGame.TeamColor.BLACK, ChessPiece.PieceType.KING));
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder boardAsString = new StringBuilder("ChessBoard{board=");
        for (int row=1; row<9;row++){
            for (int col=1;col<9;col++){
                if (board[row][col] == null){
                    boardAsString.append("null");
                    continue;
                }
                boardAsString.append(board[row][col].toString());
            }
        }
        return boardAsString.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(board, that.board);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }


}
