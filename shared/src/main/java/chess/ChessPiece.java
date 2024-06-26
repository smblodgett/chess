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
                List<Boolean> directionsBishop = new ArrayList<>(); // represents direction vectors bishop can move: (-1,-1);(-1,1);(1,-1);(1,1)
                // add the booleans to the list
                for (int j=0;j<4;j++) {
                    directionsBishop.add(true);
                }
                // go through the possible distances the bishops can move
                for (int i=1;i<=7;i++){
                    int currRow = myPosition.getRow();
                    int currCol = myPosition.getColumn();
                    int[] directionRow = {-1, 1};
                    int[] directionCol = {-1, 1};
                    // go through direction vectors
                    int ind = 0;
                    for (int diR : directionRow) {
                        for (int diC : directionCol) {
                            ChessPosition move = new ChessPosition(currRow + i*diR, currCol + i*diC);
                            // if off-board or friendly piece, end that direction
                            if (!move.onBoard()||(board.getPiece(move)!=null && board.getPiece(move).pieceColor==myColor)){
                                if (diR==-1&&diC==-1){
                                    directionsBishop.set(0,false);
                                }
                                if (diR==-1&&diC==1){
                                    directionsBishop.set(1,false);
                                }
                                if (diR==1&&diC==-1){
                                    directionsBishop.set(2,false);
                                }
                                if (diR==1&&diC==1){
                                    directionsBishop.set(3,false);
                                }
                            }
                            // add the move if the direction hasn't been cut off
                            if (directionsBishop.get(ind)) {
                                moves.add(new ChessMove(myPosition,move,null));
                            }
                            // if it's an enemy piece, make sure you can't move any further (after taking)
                            if (move.onBoard()&& (board.getPiece(move)!=null && board.getPiece(move).pieceColor!=myColor)) {
                                if (diR == -1 && diC == -1) {
                                    directionsBishop.set(0, false);
                                }
                                if (diR == -1 && diC == 1) {
                                    directionsBishop.set(1, false);
                                }
                                if (diR == 1 && diC == -1) {
                                    directionsBishop.set(2, false);
                                }
                                if (diR == 1 && diC == 1) {
                                    directionsBishop.set(3, false);
                                }
                            }
                            ind++;
                        }
                    }
                }
                break;
            case ROOK:
                List<Boolean> directionsRook = new ArrayList<>();
                for (int j=0;j<4;j++) {
                    directionsRook.add(true);
                }
                for (int i=1;i<=7;i++){
                    int currRow = myPosition.getRow();
                    int currCol = myPosition.getColumn();
                    ChessPosition posRookMoveRow = new ChessPosition(currRow+i,currCol);
                    ChessPosition posRookMoveCol = new ChessPosition(currRow,currCol+i);
                    ChessPosition negRookMoveRow = new ChessPosition(currRow-i,currCol);
                    ChessPosition negRookMoveCol = new ChessPosition(currRow,currCol-i);
                    if (directionsRook.get(0) && posRookMoveRow.onBoard() && (board.noPiece(posRookMoveRow)||board.getPiece(posRookMoveRow).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, posRookMoveRow,null));
                        if (!board.noPiece(posRookMoveRow)){
                            directionsRook.set(0,false);
                        }
                    }
                    if (directionsRook.get(1) && posRookMoveCol.onBoard() && (board.noPiece(posRookMoveCol)||board.getPiece(posRookMoveCol).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, posRookMoveCol,null));
                        if (!board.noPiece(posRookMoveCol)){
                            directionsRook.set(1,false);
                        }
                    }
                    if (directionsRook.get(2) && negRookMoveRow.onBoard() && (board.noPiece(negRookMoveRow)||board.getPiece(negRookMoveRow).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, negRookMoveRow,null));
                        if (!board.noPiece(negRookMoveRow)){
                            directionsRook.set(2,false);
                        }
                    }
                    if (directionsRook.get(3) && negRookMoveCol.onBoard() && (board.noPiece(negRookMoveCol)||board.getPiece(negRookMoveCol).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, negRookMoveCol,null));
                        if (!board.noPiece(negRookMoveCol)){
                            directionsRook.set(3,false);
                        }
                    }
                    if (directionsRook.get(0) && posRookMoveRow.onBoard() && !board.noPiece(posRookMoveRow) && board.getPiece(posRookMoveRow).pieceColor==myColor) {
                            directionsRook.set(0,false);
                    }
                    if (directionsRook.get(1) && posRookMoveCol.onBoard() && !board.noPiece(posRookMoveCol) && board.getPiece(posRookMoveCol).pieceColor==myColor) {
                        directionsRook.set(1,false);
                    }
                    if (directionsRook.get(2) && negRookMoveRow.onBoard() && !board.noPiece(negRookMoveRow) && board.getPiece(negRookMoveRow).pieceColor==myColor) {
                        directionsRook.set(2,false);
                    }
                    if (directionsRook.get(3) && negRookMoveCol.onBoard() && !board.noPiece(negRookMoveCol) && board.getPiece(negRookMoveCol).pieceColor==myColor) {
                        directionsRook.set(3,false);
                    }
                }
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
