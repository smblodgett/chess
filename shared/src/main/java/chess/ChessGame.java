package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor currentTeam;
    private ChessBoard currentBoard = new ChessBoard();
    private ChessBoard testBoard;

    public ChessGame() {
        currentBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        testBoard = currentBoard;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currentTeam;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currentTeam=team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        Set<ChessMove> finalMoves = new HashSet<ChessMove>();
        ChessBoard board = getBoard();
        ChessPiece chosenPiece = board.getPiece(startPosition);
        TeamColor chosenColor = chosenPiece.getTeamColor();
        // return null if chosen piece is nothing
        if (chosenPiece == null){
            return null;
        }
        Set<ChessMove> moves = (Set<ChessMove>) board.getPiece(startPosition).pieceMoves(board,startPosition);
        for (ChessMove move : moves){
            makeTestMove(move, board);
            if (!isInCheck(chosenColor)) {
                finalMoves.add(move);
            }
        }
        return finalMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessBoard board = getBoard();
        ChessPiece[][] pieceGrid = board.getPieceGrid();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = board.getPiece(start);
        if (movingPiece==null) throw new InvalidMoveException("Invalid move! No piece chosen");
        TeamColor pieceColor = movingPiece.getTeamColor();
        TeamColor currentColor = getTeamTurn();
        Collection<ChessMove> correctMoves = validMoves(start);
        if (currentColor!=pieceColor) throw new InvalidMoveException("Invalid move! Wrong color!");
        if (!correctMoves.contains(move)) throw new InvalidMoveException("Invalid move! That move is illegal");
        int startRow = start.getRow();
        int startColumn = start.getColumn();
        int endRow = end.getRow();
        int endCol = end.getColumn();
        if (move.getPromotionPiece()==null) pieceGrid[endRow][endCol] = movingPiece;
        else pieceGrid[endRow][endCol] = new ChessPiece(movingPiece.getTeamColor(),move.getPromotionPiece());
        pieceGrid[startRow][startColumn] = null;
        board.updateBoard(pieceGrid);
        if (currentColor==TeamColor.WHITE) setTeamTurn(TeamColor.BLACK);
        else setTeamTurn(TeamColor.WHITE);
    }

    public void makeTestMove(ChessMove move, ChessBoard board){
        ChessBoard boardCopy = new ChessBoard(board);
        ChessPiece[][] pieceGrid = boardCopy.getPieceGrid();
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece movingPiece = boardCopy.getPiece(start);
        int startRow = start.getRow();
        int startColumn = start.getColumn();
        int endRow = end.getRow();
        int endCol = end.getColumn();
        pieceGrid[endRow][endCol] = movingPiece;
        pieceGrid[startRow][startColumn] = null;
        boardCopy.updateBoard(pieceGrid);
        //assert boardCopy == board : "boardCopy is board! :(";
        testBoard = boardCopy;
    }

    public ChessPosition findKing(TeamColor teamColor){
        ChessBoard board = getTestBoard();
        ChessPiece[][] pieceGrid = board.getPieceGrid();
        for (int row=1;row<9;row++){
            for (int col=1;col<9;col++) {
                ChessPosition piecePosition = new ChessPosition(row,col);
                ChessPiece piece = pieceGrid[row][col];
                if (board.noPiece(piecePosition) || piece.getTeamColor()!=teamColor){
                    continue;
                }
                if (piece.getTeamColor()==teamColor && piece.getPieceType()==ChessPiece.PieceType.KING){
                    return new ChessPosition(row,col);
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessBoard board = getTestBoard();
        ChessPiece[][] pieceGrid = board.getPieceGrid();
        ChessPosition kingPosition = findKing(teamColor);
        Set<ChessMove> allMoves = new HashSet<>();
        for (int row=1;row<9;row++){
            for (int col=1;col<9;col++){
                ChessPosition piecePosition = new ChessPosition(row,col);
                if (board.noPiece(piecePosition)){
                    continue;
                }
                ChessPiece piece = pieceGrid[row][col];
                if (piece.getTeamColor()==teamColor){
                    continue;
                }
                Set<ChessMove> thisPieceMoves = (Set<ChessMove>) piece.pieceMoves(board, piecePosition);
                allMoves.addAll(thisPieceMoves);
            }
        }
        for (var move : allMoves){
            if (move.getEndPosition().equals(kingPosition)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        boolean inCheck = isInCheck(teamColor);
        ChessPosition kingPosition = findKing(teamColor);
        ChessBoard board = getTestBoard();
        ChessPiece[][] pieceGrid = board.getPieceGrid();
        Set<ChessMove> everyMove = new HashSet<>();
        if (!inCheck) return false;
        else {
            for (int row=1;row<9;row++) {
                for (int col = 1; col < 9; col++) {
                    ChessPosition piecePosition = new ChessPosition(row,col);
                    if (board.noPiece(piecePosition)){
                        continue;
                    }
                    ChessPiece piece = pieceGrid[row][col];
                    if (piece.getTeamColor()!=teamColor){
                        continue;
                    }
                    Set<ChessMove> thisPieceMoves = (Set<ChessMove>) validMoves(piecePosition);
                    everyMove.addAll(thisPieceMoves);
                    // need implementation to check every other piece and make sure they can't take or block check.

                }
            }
            return everyMove.isEmpty();
        }
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ChessBoard board = getBoard();
        ChessPiece[][] pieceGrid = board.getPieceGrid();
        Set<ChessMove> allMoves = new HashSet<>();
        for (int row=1;row<9;row++){
            for (int col=1;col<9;col++){
                ChessPosition piecePosition = new ChessPosition(row,col);
                if (board.noPiece(piecePosition)){
                    continue;
                }
                ChessPiece piece = pieceGrid[row][col];
                if (piece.getTeamColor()!=teamColor){
                    continue;
                }
                Set<ChessMove> validMoves = (Set<ChessMove>) validMoves(piecePosition);
                allMoves.addAll(validMoves);
            }
        }
        return allMoves.isEmpty();
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        currentBoard = board;
        testBoard = currentBoard;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return currentBoard;
    }

    public ChessBoard getTestBoard(){
        return testBoard;
    }
}
