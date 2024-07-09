package chess;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.lang.Math.abs;

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
    private static boolean whiteQueensideValid;
    private static boolean whiteKingsideValid;
    private static boolean blackQueensideValid;
    private static boolean blackKingsideValid;
    private static boolean enPassantable;
    private static ChessPosition enPassantPosition;

    public ChessGame() {
        currentBoard.resetBoard();
        setTeamTurn(TeamColor.WHITE);
        testBoard = currentBoard;
        whiteKingsideValid=true;
        whiteQueensideValid=true;
        blackKingsideValid=true;
        blackQueensideValid=true;
        enPassantable=false;
        enPassantPosition=null;
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

    static public boolean getWhiteKingsideCastle(){
        return whiteKingsideValid;
    }

    static public boolean getWhiteQueensideCastle(){
        return whiteQueensideValid;
    }

    static public boolean getBlackKingsideCastle(){
        return blackKingsideValid;
    }

    static public boolean getBlackQueensideCastle(){
        return blackQueensideValid;
    }

    static public boolean getEnPassantable(){
        return enPassantable;
    }

    static public ChessPosition getEnPassantPosition(){
        return enPassantPosition;
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
        // return null if chosen piece is nothing
        if (chosenPiece == null){
            return null;
        }
        boolean kingMoving = chosenPiece.getPieceType() == ChessPiece.PieceType.KING;

        TeamColor chosenColor = chosenPiece.getTeamColor();
        Set<ChessMove> moves = (Set<ChessMove>) board.getPiece(startPosition).pieceMoves(board,startPosition);
        for (ChessMove move : moves){
            if (kingMoving){
                if (abs(move.getEndPosition().getColumn()-startPosition.getColumn())==2 && ((currentTeam==TeamColor.BLACK && blackKingsideValid)||(currentTeam==TeamColor.WHITE && whiteKingsideValid))){
                    if (!isInCheck(chosenColor)){
                        if (!underAttack(new ChessPosition(startPosition.getRow(),startPosition.getColumn()+1),chosenColor)){
                            makeTestMove(move,board);
                            if (!isInCheck(chosenColor)) {
                                finalMoves.add(move);
                            }
                        }

                    }
                }
                else if (abs(move.getEndPosition().getColumn()-startPosition.getColumn())==3 && ((currentTeam==TeamColor.BLACK && blackQueensideValid)||(currentTeam==TeamColor.WHITE && whiteQueensideValid))){
                    if (!isInCheck(chosenColor)){
                        if (!underAttack(new ChessPosition(startPosition.getRow(),startPosition.getColumn()-1),chosenColor) &&
                                !underAttack(new ChessPosition(startPosition.getRow(),startPosition.getColumn()-2),chosenColor)
                        ){
                            makeTestMove(move,board);
                            if (!isInCheck(chosenColor)) {
                                finalMoves.add(move);
                            }
                        }

                    }
                }
                else {
                    makeTestMove(move, board);
                    if (!isInCheck(chosenColor)) {
                        finalMoves.add(move);
                    }
                }
            }
            else {
                makeTestMove(move, board);
                if (!isInCheck(chosenColor)) {
                    finalMoves.add(move);
                }
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

        if ((startRow==1 && startColumn==1) || (startRow==1 && startColumn==5)) whiteQueensideValid = false;
        if ((startRow==1 && startColumn==8) || (startRow==1 && startColumn==5)) whiteKingsideValid = false;
        if ((startRow==8 && startColumn==1) || (startRow==8 && startColumn==5)) blackQueensideValid = false;
        if ((startRow==8 && startColumn==8) || (startRow==8 && startColumn==5)) blackKingsideValid = false;
        boolean kingCastling = abs(endCol - startColumn) > 1;
        boolean kingsideCastle = endCol - startColumn > 0;
        boolean queensideCastle = endCol - startColumn < 0;
        boolean localWKs = whiteKingsideValid;
        boolean localWQs = whiteQueensideValid;
        boolean localBKs = blackKingsideValid;
        boolean localBQs = blackQueensideValid;

        // case: castling
        if (movingPiece.getPieceType()==chess.ChessPiece.PieceType.KING && kingCastling){
            pieceGrid[endRow][endCol] = movingPiece;
            if (kingsideCastle){
                pieceGrid[endRow][endCol-1] = new ChessPiece(currentColor, ChessPiece.PieceType.ROOK);
                pieceGrid[endRow][8] = null;
            }
            if (queensideCastle){
                pieceGrid[endRow][endCol+1] = new ChessPiece(currentColor, ChessPiece.PieceType.ROOK);
                pieceGrid[endRow][1] = null;
            }
        }

        // case: en passant
        boolean pawnEnPassanting = false;
        if (enPassantPosition!=null) {
            pawnEnPassanting = (movingPiece.getPieceType()==ChessPiece.PieceType.PAWN) &&
                    (endCol==enPassantPosition.getColumn()) && (((endRow==enPassantPosition.getRow()+1) &&
                            (movingPiece.getTeamColor()==TeamColor.WHITE)) || (((endRow==enPassantPosition.getRow()-1) &&
                            (movingPiece.getTeamColor()==TeamColor.BLACK))));
        }
        if (pawnEnPassanting){
            pieceGrid[endRow][endCol] = movingPiece;
            switch (movingPiece.getTeamColor()){
                case BLACK -> pieceGrid[endRow+1][endCol] = null;
                case WHITE -> pieceGrid[endRow-1][endCol] = null;
            }
        }

        // case: non-king, non-promotion
        if (move.getPromotionPiece()==null) pieceGrid[endRow][endCol] = movingPiece;
        // case: pawn promotion
        else pieceGrid[endRow][endCol] = new ChessPiece(movingPiece.getTeamColor(),move.getPromotionPiece());
        // wipe the start position
        pieceGrid[startRow][startColumn] = null;
        // update the board
        board.updateBoard(pieceGrid);
        // set the game board with board, update test board
        setBoard(board);
        whiteKingsideValid = localWKs;
        whiteQueensideValid = localWQs;
        blackKingsideValid = localBKs;
        blackQueensideValid = localBQs;

        // allow for en passant if it was a pawn that moved 2 spaces
        enPassantable=movingPiece.getPieceType()==chess.ChessPiece.PieceType.PAWN && abs(startRow-endRow)>1;
        // if so, save where that pawn is
        if (enPassantable){
            enPassantPosition= new ChessPosition(endRow,endCol);
        }
        else enPassantPosition=null;
        // change whose turn it is
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

    public boolean underAttack(ChessPosition position,TeamColor myColor) {
        ChessBoard board = getTestBoard();
        ChessPiece[][] pieceGrid = board.getPieceGrid();
        Set<ChessMove> allMoves = new HashSet<>();
            for (int row=1;row<9;row++){
                for (int col=1;col<9;col++){
                    ChessPosition piecePosition = new ChessPosition(row,col);
                    if (board.noPiece(piecePosition)){
                        continue;
                    }
                    if (board.getPiece(piecePosition).getTeamColor()==myColor) continue;
                    ChessPiece piece = pieceGrid[row][col];

                    Set<ChessMove> thisPieceMoves = (Set<ChessMove>) piece.pieceMoves(board, piecePosition);
                    allMoves.addAll(thisPieceMoves);
                }
            }
            for (var move : allMoves){
                if (move.getEndPosition().equals(position)){
                    return true;
                }
        }
            return false;
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
                    if (thisPieceMoves==null) continue;
                    everyMove.addAll(thisPieceMoves);

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
        if (isInCheckmate(teamColor)) return false;
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
                if (validMoves==null) continue;
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
        whiteKingsideValid=true;
        whiteQueensideValid=true;
        blackKingsideValid=true;
        blackQueensideValid=true;
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
