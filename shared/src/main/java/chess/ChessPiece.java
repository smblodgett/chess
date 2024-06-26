package chess;

import java.util.*;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private PieceType PieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
    this.pieceColor = pieceColor;
    this.PieceType = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return PieceType;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece chosenPiece = board.getPiece(myPosition); // get the piece at position
        ChessGame.TeamColor myColor = pieceColor;
        // ensure there is actually a piece here...
        if (chosenPiece == null){
            return null;
        }
        Set<ChessMove> moves = new HashSet<>();
        switch(chosenPiece.PieceType){
            case KNIGHT:
                for (int rowadd=1;rowadd<=2;rowadd++){
                    for (int coladd=1;coladd<=2;coladd++){
                        if (rowadd==coladd){
                            continue;
                        }
                        int currRow = myPosition.getRow();
                        int currCol = myPosition.getColumn();
                        ChessPosition q1 = new ChessPosition(currRow+rowadd,currCol+coladd);
                        ChessPosition q2 = new ChessPosition(currRow-rowadd,currCol+coladd);
                        ChessPosition q3 = new ChessPosition(currRow-rowadd,currCol-coladd);
                        ChessPosition q4 = new ChessPosition(currRow+rowadd,currCol-coladd);
                        //System.out.println(board.getPiece(q1));
                        if (q1.onBoard() && (board.getPiece(q1)==null||board.getPiece(q1).pieceColor!=myColor)) moves.add(new ChessMove(myPosition,q1,null));
                        if (q2.onBoard() && (board.getPiece(q2)==null||board.getPiece(q2).pieceColor!=myColor)) moves.add(new ChessMove(myPosition,q2,null));
                        if (q3.onBoard() && (board.getPiece(q3)==null||board.getPiece(q3).pieceColor!=myColor)) moves.add(new ChessMove(myPosition,q3,null));
                        if (q4.onBoard() && (board.getPiece(q4)==null||board.getPiece(q4).pieceColor!=myColor)) moves.add(new ChessMove(myPosition,q4,null));
                    }
                }
                break;
            case BISHOP:
                for (int i=1;i<=7;i++){
                    int currRow = myPosition.getRow();
                    int currCol = myPosition.getColumn();
                    int[] directionRow = {-1, 1};
                    int[] directionCol = {-1, 1};
                    for (int diR : directionRow) {
                        for (int diC : directionCol) {
                            ChessPosition move = new ChessPosition(currRow + i, currCol + i);
                        }
                    }
                    ChessPosition q1 = new ChessPosition(currRow+i,currCol+i);
                    ChessPosition q2 = new ChessPosition(currRow-i,currCol+i);
                    ChessPosition q3 = new ChessPosition(currRow-i,currCol-i);
                    ChessPosition q4 = new ChessPosition(currRow+i,currCol-i);
                }
                break;
            case ROOK:
                break;
            case QUEEN:
                break;
            case KING:
                break;
            case PAWN:
                break;



        }


        return moves;
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", PieceType=" + PieceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && PieceType == that.PieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, PieceType);
    }
}
