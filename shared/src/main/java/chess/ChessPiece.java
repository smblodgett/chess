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
        int currRow = myPosition.getRow();
        int currCol = myPosition.getColumn();
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
                List<Boolean> directionsQueenRank = new ArrayList<>();
                for (int j=0;j<4;j++) {
                    directionsQueenRank.add(true);
                }
                for (int i=1;i<=7;i++){
                    ChessPosition posQueenMoveRow = new ChessPosition(currRow+i,currCol);
                    ChessPosition posQueenMoveCol = new ChessPosition(currRow,currCol+i);
                    ChessPosition negQueenMoveRow = new ChessPosition(currRow-i,currCol);
                    ChessPosition negQueenMoveCol = new ChessPosition(currRow,currCol-i);
                    if (directionsQueenRank.get(0) && posQueenMoveRow.onBoard() && (board.noPiece(posQueenMoveRow)||board.getPiece(posQueenMoveRow).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, posQueenMoveRow,null));
                        if (!board.noPiece(posQueenMoveRow)){
                            directionsQueenRank.set(0,false);
                        }
                    }
                    if (directionsQueenRank.get(1) && posQueenMoveCol.onBoard() && (board.noPiece(posQueenMoveCol)||board.getPiece(posQueenMoveCol).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, posQueenMoveCol,null));
                        if (!board.noPiece(posQueenMoveCol)){
                            directionsQueenRank.set(1,false);
                        }
                    }
                    if (directionsQueenRank.get(2) && negQueenMoveRow.onBoard() && (board.noPiece(negQueenMoveRow)||board.getPiece(negQueenMoveRow).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, negQueenMoveRow,null));
                        if (!board.noPiece(negQueenMoveRow)){
                            directionsQueenRank.set(2,false);
                        }
                    }
                    if (directionsQueenRank.get(3) && negQueenMoveCol.onBoard() && (board.noPiece(negQueenMoveCol)||board.getPiece(negQueenMoveCol).pieceColor!=myColor)) {
                        moves.add(new ChessMove(myPosition, negQueenMoveCol,null));
                        if (!board.noPiece(negQueenMoveCol)){
                            directionsQueenRank.set(3,false);
                        }
                    }
                    if (directionsQueenRank.get(0) && posQueenMoveRow.onBoard() && !board.noPiece(posQueenMoveRow) && board.getPiece(posQueenMoveRow).pieceColor==myColor) {
                        directionsQueenRank.set(0,false);
                    }
                    if (directionsQueenRank.get(1) && posQueenMoveCol.onBoard() && !board.noPiece(posQueenMoveCol) && board.getPiece(posQueenMoveCol).pieceColor==myColor) {
                        directionsQueenRank.set(1,false);
                    }
                    if (directionsQueenRank.get(2) && negQueenMoveRow.onBoard() && !board.noPiece(negQueenMoveRow) && board.getPiece(negQueenMoveRow).pieceColor==myColor) {
                        directionsQueenRank.set(2,false);
                    }
                    if (directionsQueenRank.get(3) && negQueenMoveCol.onBoard() && !board.noPiece(negQueenMoveCol) && board.getPiece(negQueenMoveCol).pieceColor==myColor) {
                        directionsQueenRank.set(3,false);
                    }
                }
                List<Boolean> directionsQueenDiagonal = new ArrayList<>(); // represents direction vectors bishop can move: (-1,-1);(-1,1);(1,-1);(1,1)
                // add the booleans to the list
                for (int j=0;j<4;j++) {
                    directionsQueenDiagonal.add(true);
                }
                // go through the possible distances the bishops can move
                for (int i=1;i<=7;i++){
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
                                    directionsQueenDiagonal.set(0,false);
                                }
                                if (diR==-1&&diC==1){
                                    directionsQueenDiagonal.set(1,false);
                                }
                                if (diR==1&&diC==-1){
                                    directionsQueenDiagonal.set(2,false);
                                }
                                if (diR==1&&diC==1){
                                    directionsQueenDiagonal.set(3,false);
                                }
                            }
                            // add the move if the direction hasn't been cut off
                            if (directionsQueenDiagonal.get(ind)) {
                                moves.add(new ChessMove(myPosition,move,null));
                            }
                            // if it's an enemy piece, make sure you can't move any further (after taking)
                            if (move.onBoard()&& (board.getPiece(move)!=null && board.getPiece(move).pieceColor!=myColor)) {
                                if (diR == -1 && diC == -1) {
                                    directionsQueenDiagonal.set(0, false);
                                }
                                if (diR == -1 && diC == 1) {
                                    directionsQueenDiagonal.set(1, false);
                                }
                                if (diR == 1 && diC == -1) {
                                    directionsQueenDiagonal.set(2, false);
                                }
                                if (diR == 1 && diC == 1) {
                                    directionsQueenDiagonal.set(3, false);
                                }
                            }
                            ind++;
                        }
                    }
                }
                break;
            case KING:
                int[][] moveSet = {{1,1},{1,0},{1,-1},{0,1},{0,-1},{-1,1},{-1,0},{-1,-1}};
                for (var mv : moveSet){
                    ChessPosition move = new ChessPosition(currRow + mv[0], currCol + mv[1]);
                    if (move.onBoard() && (board.noPiece(move) || board.getPiece(move).pieceColor!=myColor)){
                        moves.add(new ChessMove(myPosition,move,null));
                    }
                    else if (!move.onBoard() || board.getPiece(move).pieceColor==myColor){
                        continue;
                    }
                }
                if (myColor==ChessGame.TeamColor.WHITE && currRow==1 && currCol==5){
                    if (ChessGame.getWhiteKingsideCastle()){
                        ChessPosition f1 = new ChessPosition(currRow,currCol+1);
                        ChessPosition g1 = new ChessPosition(currRow,currCol+2);
                        if (board.noPiece(f1) && board.noPiece(g1)){
                            ChessMove kingsideCastle = new ChessMove(myPosition,g1,null);
                            moves.add(kingsideCastle);
                        }
                    }
                    if (ChessGame.getWhiteQueensideCastle()){
                        ChessPosition b1 = new ChessPosition(currRow,currCol-3);
                        ChessPosition c1 = new ChessPosition(currRow,currCol-2);
                        ChessPosition d1 = new ChessPosition(currRow,currCol-1);
                        if (board.noPiece(b1) && board.noPiece(c1) && board.noPiece(d1)){
                            ChessMove queensideCastle = new ChessMove(myPosition,c1,null);
                            moves.add(queensideCastle);
                        }
                    }
                }
                if (myColor==ChessGame.TeamColor.BLACK && currRow==8 && currCol==5){
                    if (ChessGame.getBlackKingsideCastle()){
                        ChessPosition f8 = new ChessPosition(currRow,currCol+1);
                        ChessPosition g8 = new ChessPosition(currRow,currCol+2);
                        if (board.noPiece(f8) && board.noPiece(g8)){
                            ChessMove kingsideCastle = new ChessMove(myPosition,g8,null);
                            moves.add(kingsideCastle);
                        }
                    }
                    if (ChessGame.getBlackQueensideCastle()){ChessPosition b8 = new ChessPosition(currRow,currCol-3);
                        ChessPosition c8 = new ChessPosition(currRow,currCol-2);
                        ChessPosition d8 = new ChessPosition(currRow,currCol-1);
                        if (board.noPiece(b8) && board.noPiece(c8) && board.noPiece(d8)){
                            ChessMove queensideCastle = new ChessMove(myPosition,c8,null);
                            moves.add(queensideCastle);
                        }
                    }
                }


                break;
            case PAWN:
                // check if pawn is on 2nd/7th rank, make 2 move flag
                boolean firstMove = (myColor == ChessGame.TeamColor.WHITE && currRow == 2)
                        || (myColor == ChessGame.TeamColor.BLACK && currRow == 7);

                // capturing flags
                ChessPosition whiteCapturePosR = new ChessPosition(currRow+1,currCol+1);
                ChessPosition whiteCapturePosL = new ChessPosition(currRow+1,currCol-1);
                ChessPosition blackCapturePosR = new ChessPosition(currRow-1,currCol+1);
                ChessPosition blackCapturePosL = new ChessPosition(currRow-1,currCol-1);
                boolean whiteCanCaptureR = (whiteCapturePosR.onBoard() && !board.noPiece(whiteCapturePosR) && board.getPiece(whiteCapturePosR).pieceColor!=myColor);
                boolean whiteCanCaptureL = (whiteCapturePosL.onBoard() && !board.noPiece(whiteCapturePosL) && board.getPiece(whiteCapturePosL).pieceColor!=myColor);
                boolean blackCanCaptureR = (blackCapturePosR.onBoard() && !board.noPiece(blackCapturePosR) && board.getPiece(blackCapturePosR).pieceColor!=myColor);
                boolean blackCanCaptureL = (blackCapturePosL.onBoard() && !board.noPiece(blackCapturePosL) && board.getPiece(blackCapturePosL).pieceColor!=myColor);

                if (myColor==ChessGame.TeamColor.WHITE){
                    boolean pieceInFront = (!board.noPiece(new ChessPosition(currRow+1,currCol)));
                    boolean pieceTwoInFront = (firstMove && (pieceInFront || !board.noPiece(new ChessPosition(currRow+2,currCol))));
                    boolean promotionReached = (currRow+1==8);
                    if (!pieceInFront && !promotionReached) {
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol),null));
                    }
                    if (!pieceInFront && promotionReached) {
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol), PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol), PieceType.KNIGHT));
                    }
                    if (firstMove && !pieceTwoInFront){
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow+2,currCol),null));
                    }
                    if (whiteCanCaptureL){
                        if (!promotionReached){
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol-1),null));
                        }
                        else {
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol-1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol-1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol-1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol-1), PieceType.KNIGHT));
                        }
                    }
                    if (whiteCanCaptureR){
                        if (!promotionReached){
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol+1),null));
                        }
                        else {
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol+1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol+1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol+1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow+1,currCol+1), PieceType.KNIGHT));
                        }
                    }

                }
                if (myColor==ChessGame.TeamColor.BLACK){
                    boolean pieceInFront = (!board.noPiece(new ChessPosition(currRow-1,currCol)));
                    boolean pieceTwoInFront = (firstMove && (pieceInFront || !board.noPiece(new ChessPosition(currRow-2,currCol))));
                    boolean promotionReached = (currRow-1==1);
                    if (!pieceInFront && !promotionReached) {
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol),null));
                    }
                    if (!pieceInFront && promotionReached) {
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol), PieceType.QUEEN));
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol), PieceType.ROOK));
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol), PieceType.BISHOP));
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol), PieceType.KNIGHT));
                    }
                    if (firstMove && !pieceTwoInFront){
                        moves.add(new ChessMove(myPosition,new ChessPosition(currRow-2,currCol),null));
                    }
                    if (blackCanCaptureL){
                        if (!promotionReached){
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol-1),null));
                        }
                        else {
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol-1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol-1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol-1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol-1), PieceType.KNIGHT));
                        }
                    }
                    if (blackCanCaptureR){
                        if (!promotionReached){
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol+1),null));
                        }
                        else {
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol+1), PieceType.QUEEN));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol+1), PieceType.ROOK));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol+1), PieceType.BISHOP));
                            moves.add(new ChessMove(myPosition,new ChessPosition(currRow-1,currCol+1), PieceType.KNIGHT));
                        }
                    }
                }
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
