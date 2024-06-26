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
    // question: do I need to declare this up here?

    public ChessBoard() {
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

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        board = new ChessPiece[9][9]; // clear everything from board
        // add the rows of pawns
        //System.out.println("board has been reset!!");
        for (int i=1;i<=8;i++){
            // add white pawns
            ChessPosition whitePawnPos = new ChessPosition(2,i);
            ChessPiece whitePawn = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.PAWN);
            addPiece(whitePawnPos,whitePawn);

            // add black pawns
            ChessPosition blackPawnPos = new ChessPosition(7,i);
            ChessPiece blackPawn = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.PAWN);
            addPiece(blackPawnPos,blackPawn);
        }

        // add knights
        for (int i=1;i<=8;i++){
            // knights only belong on the 2nd and 7th files
            if (i==1||i==3||i==4||i==5||i==6||i==8) {
                continue;
            }
            // add white knights
            ChessPosition whiteKnightPos = new ChessPosition(1,i);
            ChessPiece whiteKnight = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KNIGHT);
            addPiece(whiteKnightPos,whiteKnight);

            // add black knights
            ChessPosition blackKnightPos = new ChessPosition(8,i);
            ChessPiece blackKnight = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KNIGHT);
            addPiece(blackKnightPos,blackKnight);
        }

        // add bishops
        for (int i=1;i<=8;i++){
            // bishops only belong on the 3rd and 6th files
            if (i==1||i==2||i==4||i==5||i==7||i==8) {
                continue;
            }
            // add white bishops
            ChessPosition whiteBishopPos = new ChessPosition(1,i);
            ChessPiece whiteBishop = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.BISHOP);
            addPiece(whiteBishopPos,whiteBishop);

            // add black bishops
            ChessPosition blackBishopPos = new ChessPosition(8,i);
            ChessPiece blackBishop = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.BISHOP);
            addPiece(blackBishopPos,blackBishop);
        }

        // add rooks
        for (int i=1;i<=8;i++){
            // rooks only belong on the first and eighth files
            if (i==2||i==3||i==4||i==5||i==6||i==7) {
                continue;
            }
            // add white rooks
            ChessPosition whiteRookPos = new ChessPosition(1,i);
            ChessPiece whiteRook = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.ROOK);
            addPiece(whiteRookPos,whiteRook);

            // add black rooks
            ChessPosition blackRookPos = new ChessPosition(8,i);
            ChessPiece blackRook = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.ROOK);
            addPiece(blackRookPos,blackRook);
        }

        // add queens
        for (int i=1;i<=8;i++){
            // queens only belong on the 4th file
            if (i==1||i==2||i==3||i==5||i==6||i==7||i==8) {
                continue;
            }
            // add white queen
            ChessPosition whiteQueenPos = new ChessPosition(1,i);
            ChessPiece whiteQueen = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.QUEEN);
            addPiece(whiteQueenPos,whiteQueen);

            // add black queen
            ChessPosition blackQueenPos = new ChessPosition(8,i);
            ChessPiece blackQueen = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.QUEEN);
            addPiece(blackQueenPos,blackQueen);
        }

        // add kings
        for (int i=1;i<=8;i++){
            // kings only belong on the 5th file
            if (i==1||i==2||i==3||i==4||i==6||i==7||i==8) {
                continue;
            }
            // add white king
            ChessPosition whiteKingPos = new ChessPosition(1,i);
            ChessPiece whiteKing = new ChessPiece(ChessGame.TeamColor.WHITE,ChessPiece.PieceType.KING);
            addPiece(whiteKingPos,whiteKing);

            // add black king
            ChessPosition blackKingPos = new ChessPosition(8,i);
            ChessPiece blackKing = new ChessPiece(ChessGame.TeamColor.BLACK,ChessPiece.PieceType.KING);
            addPiece(blackKingPos,blackKing);
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
