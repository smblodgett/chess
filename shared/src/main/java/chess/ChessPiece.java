package chess;

import java.util.*;

import static chess.ChessGame.*;
import static java.lang.Math.abs;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    private final ChessGame.TeamColor pieceColor;
    private PieceType pieceType;


    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.pieceType = type;
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
        return pieceType;
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
        if (chosenPiece == null) {
            return null;
        }
        Set<ChessMove> moves = new HashSet<>();
        switch (chosenPiece.pieceType) {
            case KNIGHT:
                moves = knightMoves(currRow, currCol, myPosition, myColor, board, moves);
                break;
            case BISHOP:
                moves = bishopMoves(currRow, currCol, myPosition, myColor, board, moves);
                break;
            case ROOK:
                moves = rookMoves(currRow, currCol, myPosition, myColor, board, moves);
                break;
            case QUEEN:
                moves = queenMoves(currRow, currCol, myPosition, myColor, board, moves);
                break;
            case KING:
                moves = kingMoves(currRow, currCol, myPosition, myColor, board, moves);
                break;
            case PAWN:
                moves = pawnMoves(currRow, currCol, myPosition, myColor, board, moves);
                break;
        }
        return moves;
    }

    public Set<ChessMove> knightMoves(int currRow, int currCol, ChessPosition myPosition, TeamColor myColor, ChessBoard board, Set<ChessMove> moves) {
        for (int rowadd = 1; rowadd <= 2; rowadd++) {
            for (int coladd = 1; coladd <= 2; coladd++) {
                if (rowadd == coladd) {
                    continue;
                }
                ChessPosition q1 = new ChessPosition(currRow + rowadd, currCol + coladd);
                ChessPosition q2 = new ChessPosition(currRow - rowadd, currCol + coladd);
                ChessPosition q3 = new ChessPosition(currRow - rowadd, currCol - coladd);
                ChessPosition q4 = new ChessPosition(currRow + rowadd, currCol - coladd);
                //System.out.println(board.getPiece(q1));
                if (q1.onBoard() && (board.getPiece(q1) == null || board.getPiece(q1).pieceColor != myColor)){
                    moves.add(new ChessMove(myPosition, q1, null));}
                if (q2.onBoard() && (board.getPiece(q2) == null || board.getPiece(q2).pieceColor != myColor)){
                    moves.add(new ChessMove(myPosition, q2, null));}
                if (q3.onBoard() && (board.getPiece(q3) == null || board.getPiece(q3).pieceColor != myColor)){
                    moves.add(new ChessMove(myPosition, q3, null));}
                if (q4.onBoard() && (board.getPiece(q4) == null || board.getPiece(q4).pieceColor != myColor)){
                    moves.add(new ChessMove(myPosition, q4, null));}
            }
        }
        return moves;
    }

    public Set<ChessMove> bishopMoves(int currRow, int currCol, ChessPosition myPosition, TeamColor myColor, ChessBoard board, Set<ChessMove> moves) {
        int[][] bishopDirections = {{1,1},{1,-1},{-1,1},{-1,-1}};
        boolean[] bishopFlag = {true,true,true,true};
        for (int i=1;i<8;i++){
            int ind = 0;
            for (var dir : bishopDirections){
                if (!bishopFlag[ind]){
                    ind++;
                    continue;
                }
                ChessPosition mayPosition = new ChessPosition(currRow+dir[0]*i,currCol+dir[1]*i);
                ChessMove mayMove = new ChessMove(myPosition,mayPosition,null);
                if (!mayPosition.onBoard()){
                    bishopFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.isEnemyPiece(mayPosition,pieceColor)){
                    moves.add(mayMove);
                    bishopFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && !board.noPiece(mayPosition) && !board.isEnemyPiece(mayPosition,pieceColor)){
                    bishopFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.noPiece(mayPosition)){
                    moves.add(mayMove);
                }
                ind++;
            }
        }
        return moves;
    }

    public Set<ChessMove> rookMoves(int currRow, int currCol, ChessPosition myPosition, TeamColor myColor, ChessBoard board, Set<ChessMove> moves) {
        int[][] rookDirections = {{1,0},{0,-1},{-1,0},{0,1}};
        boolean[] rookFlag = {true,true,true,true};
        for (int i=1;i<8;i++){
            int ind = 0;
            for (var dir : rookDirections){
                if (!rookFlag[ind]){
                    ind++;
                    continue;
                }
                ChessPosition mayPosition = new ChessPosition(currRow+dir[0]*i,currCol+dir[1]*i);
                ChessMove mayMove = new ChessMove(myPosition,mayPosition,null);
                if (!mayPosition.onBoard()){
                    rookFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.isEnemyPiece(mayPosition,pieceColor)){
                    moves.add(mayMove);
                    rookFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && !board.noPiece(mayPosition) && !board.isEnemyPiece(mayPosition,pieceColor)){
                    rookFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.noPiece(mayPosition)){
                    moves.add(mayMove);
                }
                ind++;
            }
        }
        return moves;
    }

    public Set<ChessMove> queenMoves(int currRow, int currCol, ChessPosition myPosition, TeamColor myColor, ChessBoard board, Set<ChessMove> moves) {
        int[][] queenDiagDirections = {{1,1},{1,-1},{-1,1},{-1,-1}};
        boolean[] queenDiagFlag = {true,true,true,true};
        for (int i=1;i<8;i++){
            int ind = 0;
            for (var dir : queenDiagDirections){
                if (!queenDiagFlag[ind]){
                    ind++;
                    continue;
                }
                ChessPosition mayPosition = new ChessPosition(currRow+dir[0]*i,currCol+dir[1]*i);
                ChessMove mayMove = new ChessMove(myPosition,mayPosition,null);
                if (!mayPosition.onBoard()){
                    queenDiagFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.isEnemyPiece(mayPosition,pieceColor)){
                    moves.add(mayMove);
                    queenDiagFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && !board.noPiece(mayPosition) && !board.isEnemyPiece(mayPosition,pieceColor)){
                    queenDiagFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.noPiece(mayPosition)){
                    moves.add(mayMove);
                }
                ind++;
            }
        }
        int[][] queenStraightDirections = {{1,0},{0,-1},{-1,0},{0,1}};
        boolean[] queenStraightFlag = {true,true,true,true};
        for (int i=1;i<8;i++){
            int ind = 0;
            for (var dir : queenStraightDirections){
                if (!queenStraightFlag[ind]){
                    ind++;
                    continue;
                }
                ChessPosition mayPosition = new ChessPosition(currRow+dir[0]*i,currCol+dir[1]*i);
                ChessMove mayMove = new ChessMove(myPosition,mayPosition,null);
                if (!mayPosition.onBoard()){
                    queenStraightFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.isEnemyPiece(mayPosition,pieceColor)){
                    moves.add(mayMove);
                    queenStraightFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && !board.noPiece(mayPosition) && !board.isEnemyPiece(mayPosition,pieceColor)){
                    queenStraightFlag[ind] = false;
                }
                else if (mayPosition.onBoard() && board.noPiece(mayPosition)){
                    moves.add(mayMove);
                }
                ind++;
            }
        }
        return moves;
    }

    public Set<ChessMove> kingMoves(int currRow, int currCol, ChessPosition myPosition, TeamColor myColor, ChessBoard board, Set<ChessMove> moves) {
        int[][] moveSet = {{1, 1}, {1, 0}, {1, -1}, {0, 1}, {0, -1}, {-1, 1}, {-1, 0}, {-1, -1}};
        for (var mv : moveSet) {
            ChessPosition move = new ChessPosition(currRow + mv[0], currCol + mv[1]);
            if (move.onBoard() && (board.noPiece(move) || board.getPiece(move).pieceColor != myColor)) {
                moves.add(new ChessMove(myPosition, move, null));
            } else if (!move.onBoard() || board.getPiece(move).pieceColor == myColor) {
                continue;
            }
        }
        if (myColor == ChessGame.TeamColor.WHITE && currRow == 1 && currCol == 5) {
            if (ChessGame.getWhiteKingsideCastle()) {
                ChessPosition f1 = new ChessPosition(currRow, currCol + 1);
                ChessPosition g1 = new ChessPosition(currRow, currCol + 2);
                if (board.noPiece(f1) && board.noPiece(g1)) {
                    ChessMove kingsideCastle = new ChessMove(myPosition, g1, null);
                    moves.add(kingsideCastle);
                }
            }
            if (ChessGame.getWhiteQueensideCastle()) {
                ChessPosition b1 = new ChessPosition(currRow, currCol - 3);
                ChessPosition c1 = new ChessPosition(currRow, currCol - 2);
                ChessPosition d1 = new ChessPosition(currRow, currCol - 1);
                if (board.noPiece(b1) && board.noPiece(c1) && board.noPiece(d1)) {
                    ChessMove queensideCastle = new ChessMove(myPosition, c1, null);
                    moves.add(queensideCastle);
                }
            }
        }
        if (myColor == ChessGame.TeamColor.BLACK && currRow == 8 && currCol == 5) {
            if (ChessGame.getBlackKingsideCastle()) {
                ChessPosition f8 = new ChessPosition(currRow, currCol + 1);
                ChessPosition g8 = new ChessPosition(currRow, currCol + 2);
                if (board.noPiece(f8) && board.noPiece(g8)) {
                    ChessMove kingsideCastle = new ChessMove(myPosition, g8, null);
                    moves.add(kingsideCastle);
                }
            }
            if (ChessGame.getBlackQueensideCastle()) {
                ChessPosition b8 = new ChessPosition(currRow, currCol - 3);
                ChessPosition c8 = new ChessPosition(currRow, currCol - 2);
                ChessPosition d8 = new ChessPosition(currRow, currCol - 1);
                if (board.noPiece(b8) && board.noPiece(c8) && board.noPiece(d8)) {
                    ChessMove queensideCastle = new ChessMove(myPosition, c8, null);
                    moves.add(queensideCastle);
                }
            }
        }
        return moves;
    }

    Set<ChessMove> pawnMoves(int currRow, int currCol, ChessPosition myPosition,TeamColor myColor, ChessBoard board, Set<ChessMove> moves){
        // check if pawn is on 2nd/7th rank, make 2 move flag
        boolean firstMove = (myColor == ChessGame.TeamColor.WHITE && currRow == 2)
                || (myColor == ChessGame.TeamColor.BLACK && currRow == 7);
        // capturing flags
        ChessPosition whiteCapturePosR = new ChessPosition(currRow + 1, currCol + 1);
        ChessPosition whiteCapturePosL = new ChessPosition(currRow + 1, currCol - 1);
        ChessPosition blackCapturePosR = new ChessPosition(currRow - 1, currCol + 1);
        ChessPosition blackCapturePosL = new ChessPosition(currRow - 1, currCol - 1);
        boolean whiteCanCaptureR = (whiteCapturePosR.onBoard() && !board.noPiece(whiteCapturePosR)
                                    && board.getPiece(whiteCapturePosR).pieceColor != myColor);
        boolean whiteCanCaptureL = (whiteCapturePosL.onBoard() && !board.noPiece(whiteCapturePosL)
                                    && board.getPiece(whiteCapturePosL).pieceColor != myColor);
        boolean blackCanCaptureR = (blackCapturePosR.onBoard() && !board.noPiece(blackCapturePosR)
                                    && board.getPiece(blackCapturePosR).pieceColor != myColor);
        boolean blackCanCaptureL = (blackCapturePosL.onBoard() && !board.noPiece(blackCapturePosL)
                                    && board.getPiece(blackCapturePosL).pieceColor != myColor);
        if (myColor == ChessGame.TeamColor.WHITE) { // white/black switch
            boolean pieceInFront = (!board.noPiece(new ChessPosition(currRow + 1, currCol)));
            boolean pieceTwoInFront = (firstMove && (pieceInFront || !board.noPiece(new ChessPosition(currRow + 2, currCol))));
            boolean promotionReached = (currRow + 1 == 8);
            if (!pieceInFront && !promotionReached) {
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol), null));}
            if (!pieceInFront && promotionReached) {
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol), PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol), PieceType.ROOK));
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol), PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol), PieceType.KNIGHT));
            }
            if (firstMove && !pieceTwoInFront) {
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 2, currCol), null));
            }
            if (whiteCanCaptureL) {
                if (!promotionReached) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol - 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol - 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol - 1), PieceType.KNIGHT));
                }
            }
            if (whiteCanCaptureR) {
                if (!promotionReached) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol + 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol + 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow + 1, currCol + 1), PieceType.KNIGHT));
                }
            }
            if (getEnPassantable()) {
                ChessPosition passedPawnPos = getEnPassantPosition();
                int passedPawnRow = passedPawnPos.getRow();
                int passedPawnCol = passedPawnPos.getColumn();
                ChessPosition passantEndPos = new ChessPosition(passedPawnRow + 1, passedPawnCol);
                if (passedPawnRow == currRow && abs(passedPawnCol - currCol) == 1 && passantEndPos.onBoard() && board.noPiece(passantEndPos)) {
                    moves.add(new ChessMove(myPosition, passantEndPos, null));
                }
            }

        }
        if (myColor == ChessGame.TeamColor.BLACK) {
            boolean pieceInFront = (!board.noPiece(new ChessPosition(currRow - 1, currCol)));
            boolean pieceTwoInFront = (firstMove && (pieceInFront || !board.noPiece(new ChessPosition(currRow - 2, currCol))));
            boolean promotionReached = (currRow - 1 == 1);
            if (!pieceInFront && !promotionReached) {
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol), null));}
            if (!pieceInFront && promotionReached) {
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol), PieceType.QUEEN));
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol), PieceType.ROOK));
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol), PieceType.BISHOP));
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol), PieceType.KNIGHT));}
            if (firstMove && !pieceTwoInFront) {
                moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 2, currCol), null));}
            if (blackCanCaptureL) {
                if (!promotionReached) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol - 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol - 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol - 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol - 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol - 1), PieceType.KNIGHT));} }
            if (blackCanCaptureR) {
                if (!promotionReached) {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol + 1), null));
                } else {
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol + 1), PieceType.QUEEN));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol + 1), PieceType.ROOK));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol + 1), PieceType.BISHOP));
                    moves.add(new ChessMove(myPosition, new ChessPosition(currRow - 1, currCol + 1), PieceType.KNIGHT));}}
            if (getEnPassantable()) {
                ChessPosition passedPawnPos = getEnPassantPosition();
                int passedPawnRow = passedPawnPos.getRow();
                int passedPawnCol = passedPawnPos.getColumn();
                ChessPosition passantEndPos = new ChessPosition(passedPawnRow - 1, passedPawnCol);
                if (passedPawnRow == currRow && abs(passedPawnCol - currCol) == 1 && passantEndPos.onBoard() && board.noPiece(passantEndPos)) {
                    moves.add(new ChessMove(myPosition, passantEndPos, null));} } }
        return moves;
    }

        @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", PieceType=" + pieceType +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {return true;}
        if (o == null || getClass() != o.getClass()){ return false;}
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && pieceType == that.pieceType;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, pieceType);
    }
}
