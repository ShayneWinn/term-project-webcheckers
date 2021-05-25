package com.webcheckers.model;

import com.webcheckers.util.Message;

import java.util.Stack;

/**
 * Game, controls the Board View and move actions
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='mailto:spm8848@rit.edu'>Sean McDonnell</a>
 */
public class Game {

    public enum Status {
        ACTIVE,
        RED_ABANDONED,
        WHITE_ABANDONED,
        RED_RESIGN,
        WHITE_RESIGN,
    }

    /**
     * Constants
     */
    private final static int MAX_NUM_ROWS = 8;
    private final static int MAX_NUM_COLS = 8;

    /**
     * Attributes
     */
    private final int gameID;
    private final Player redPlayer;
    private final Player whitePlayer;

    private boolean isRedPlayerTurn;
    private boolean isWhitePlayerTurn;
    private String activeColor = "RED";

    private Space[][] board;
    private BoardView boardView;
    private Message gameEndedReason = null;
    private Stack<Move> pendingMoves;
    private Stack<Space[][]> pendingMoveBoards;

    private Status gameStatus;

    /**
     * Creates a {@link Game} object that holds an Iterable board and a 2D array.
     *
     * @param gameID      The id for the {@link Game} object
     * @param redPlayer   The {@link Player} object associated with red pieces
     * @param whitePlayer The {@link Player} object associated with white pieces
     */
    public Game(final int gameID, final Player redPlayer, final Player whitePlayer) {
        this.gameID = gameID;
        this.redPlayer = redPlayer;
        this.whitePlayer = whitePlayer;
        this.board = new Space[MAX_NUM_ROWS][MAX_NUM_COLS];
        this.boardView = new BoardView();
        this.pendingMoves = new Stack();
        this.pendingMoveBoards = new Stack();

        this.isRedPlayerTurn = true;
        this.isWhitePlayerTurn = false;
        this.gameStatus = Status.ACTIVE;

        for (int rowIdx = 0; rowIdx < MAX_NUM_ROWS; rowIdx++) {
            for (int colIdx = 0; colIdx < MAX_NUM_COLS; colIdx++) {
                if (rowIdx == 0 || rowIdx == 6) {
                    if (rowIdx == 0) {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, colIdx,
                                    new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));
                        }
                    }
                    else {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, colIdx,
                                    new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));
                        }
                    }
                }
                else if (rowIdx == 1 || rowIdx == 7) {
                    if (rowIdx == 1) {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, colIdx,
                                    new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                    }
                    else {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, colIdx,
                                    new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                    }
                }
                else if (rowIdx == 2 || rowIdx == 5) {
                    if (rowIdx == 2) {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, colIdx,
                                    new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));
                        }
                    }
                    else {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.NOT_EMPTY, Space.SpaceColor.BLACK, colIdx,
                                    new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                    }
                }
                else {
                    if (rowIdx % 2 == 0) {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, colIdx, null);
                        }
                    }
                    else {
                        if (colIdx % 2 == 0) {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.BLACK, colIdx, null);
                        }
                        else {
                            board[rowIdx][colIdx] = new Space(Space.State.EMPTY, Space.SpaceColor.WHITE, colIdx, null);
                        }
                    }
                }
            }
        }
    }

    /**
     * Returns the game ID
     *
     * @return int
     */
    public int getGameID() {
        return this.gameID;
    }

    /**
     * Returns whether or not the game is still active
     *
     * @return boolean isActive
     */

    public boolean isActive() { return this.gameStatus == Status.ACTIVE; }

    /**
     * Returns if it is the red player's turn
     * 
     * @return boolean isRedPlayerTurn
     */
    public boolean isRedPlayerTurn() {
        return this.isRedPlayerTurn;
    }

    /**
     * Returns if it is the white player's turn
     * 
     * @return boolean isWhitePlayerTurn
     */
    public boolean isWhitePlayerTurn() {
        return this.isWhitePlayerTurn;
    }

    /**
     * Changes that turn from one player to the other, and clears pending boards and moves of the previous player
     */
    public void changeTurn() {
        this.isRedPlayerTurn = !isRedPlayerTurn;
        this.isWhitePlayerTurn = !isWhitePlayerTurn;
        if (isRedPlayerTurn) {
            activeColor = "RED";
        }
        else {
            activeColor = "WHITE";
        }
        clearPendingMoves();
        clearPendingMoveBoards();
    }

    /**
     * Return the color of the active player
     * @return the current player's color
     */
    public String getActiveColor() {
        return activeColor;
    }

    /**
     * Gives the reason the game ended, null if game is still active
     *
     * @return {@link Message} stating reason game ended
     */
    public Message getGameEndedReason() {
        return this.gameEndedReason;
    }

    /**
     * Ends a game for a given reason
     *
     * @param reason {@link Message} describing why the game ended
     * @return was ending the game was successful
     */
    public boolean endGame(Status reason) {
        if (this.isActive()) {
            this.gameStatus = reason;

            switch(reason) {
                case RED_ABANDONED:
                    this.gameEndedReason = new Message(this.redPlayer.getName() + " has disconnected", Message.Type.ERROR);
                    break;

                case WHITE_ABANDONED:
                    this.gameEndedReason = new Message(this.whitePlayer.getName() + " has disconnected", Message.Type.ERROR);
                    break;
                
                case RED_RESIGN:
                    this.gameEndedReason = new Message(this.redPlayer.getName() + " has resigned the match", Message.Type.ERROR);
                    break;

                case WHITE_RESIGN:
                    this.gameEndedReason = new Message(this.whitePlayer.getName() + " has resigned the match", Message.Type.ERROR);
                    break;

                default:
                    this.gameEndedReason = new Message("The game has ended", Message.Type.ERROR);
                    break;
            }
            return true;
        }
        return false;
    }

    /**
     * Gets the {@link BoardView} based on the current state of the game and player's POV
     *
     * @param player {@link Player} requesting the view
     * @return {@link BoardView}
     */
    public BoardView getBoardView(Player player) {
        boolean isRedPlayer = player == redPlayer;
        this.boardView = new BoardView();

        if (isRedPlayer) {
            for (int rowIdx = MAX_NUM_ROWS - 1; rowIdx >= 0; rowIdx--) {
                Row row = new Row(rowIdx);
                boardView.getRows().add(row);
                for (int colIdx = MAX_NUM_COLS - 1; colIdx >= 0; colIdx--) {
                    Space space = board[rowIdx][colIdx];
                    row.getSpaces().add(space);
                }
            }
        }
        else {
            for (int rowIdx = 0; rowIdx < MAX_NUM_ROWS; rowIdx++) {
                Row row = new Row(rowIdx);
                boardView.getRows().add(row);
                for (int colIdx = 0; colIdx < MAX_NUM_COLS; colIdx++) {
                    Space space = board[rowIdx][colIdx];
                    row.getSpaces().add(space);
                }
            }
        }
        return this.boardView;
    }

    /**
     * Gets both players in the game
     *
     * @return {@link Player}[2] containing white and red players
     */
    public Player[] getPlayers() {
        return new Player[] { redPlayer, whitePlayer };
    }

    /**
     * gets the board
     *
     * @return Board
     */
    public Space[][] getBoard() { return board; }

    /**
     * modifies the actual board that is used for the game
     *
     * @param board
     */
    public void modifyActualBoard(Space[][] board) { this.board = board; }

    /**
     * This function will validate a {@link Move}
     *
     * @param move
     *      A {@link Move} object containing the starting and ending coordinates of a move
     * @return
     *      A {@link Message} containing the outcome of the attempted move
     */
    public Message validateMove(Move move, Space[][] board) {
        int startRow = move.getStart().getRow();
        int startCell = move.getStart().getCell();
        int endRow = move.getEnd().getRow();
        int endCell = move.getEnd().getCell();

        if ((startRow != endRow - 2 || startCell != endCell - 2) && (startRow != endRow - 2 || startCell != endCell + 2) &&
                (startRow != endRow + 2 || startCell != endCell - 2) && (startRow != endRow + 2 || startCell != endCell + 2) &&
                (startRow != endRow - 1 || startCell != endCell - 1) && (startRow != endRow - 1 || startCell != endCell + 1) &&
                (startRow != endRow + 1 || startCell != endCell - 1) && (startRow != endRow + 1 || startCell != endCell + 1)) {
            return new Message("You cannot move that far.", Message.Type.ERROR);
        }
        else if (validateJumpMove(move, board, true)) {
            return new Message("You captured a piece.", Message.Type.INFO);
        }
        else if (validateSimpleMove(move, board, true)) {
            return new Message("You moved a piece.", Message.Type.INFO);
        }
        else {
            return new Message("That was not a valid move.", Message.Type.ERROR);
        }
    }

    /**
     * This function will modify the {@link Piece.Type} element of a {@link Piece} to KING
     *
     * @param moveEndRow the row in which a move ends
     * @param moveEndCell the column in which a move ends
     */
    public void kingMe(int moveEndRow, int moveEndCell, Space[][] board) {
        if (moveEndRow == 0 || moveEndRow == MAX_NUM_ROWS - 1) {
            board[moveEndRow][moveEndCell].getPiece().modifyTypeToKING();
        }
    }

    /**
     * This function will validate an attempted jump {@link Move}
     *
     * @param move
     *      A {@link Move} object containing the starting and ending coordinates of a move
     * @return Boolean
     */
    public boolean validateJumpMove(Move move, Space[][] board, boolean commitMove) {

        int startRow = move.getStart().getRow();
        int startCell = move.getStart().getCell();
        int endRow = move.getEnd().getRow();
        int endCell = move.getEnd().getCell();

        if (board[startRow][startCell].getPiece().getType() == Piece.Type.KING) {
            if ((startRow - 2 == endRow) && (startCell - 2 == endCell) && (board[startRow - 2][startCell - 2].getState() == Space.State.EMPTY)
                    && (board[startRow - 1][startCell - 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow - 1][startCell - 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow - 1][startCell - 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow - 2 == endRow) && (startCell + 2 == endCell) && (board[startRow - 2][startCell + 2].getState() == Space.State.EMPTY)
                    && (board[startRow - 1][startCell + 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow - 1][startCell + 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow - 1][startCell + 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow + 2 == endRow) && (startCell - 2 == endCell) && (board[startRow + 2][startCell - 2].getState() == Space.State.EMPTY)
                    && (board[startRow + 1][startCell - 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow + 1][startCell - 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow + 1][startCell - 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow + 2 == endRow) && (startCell + 2 == endCell) && (board[startRow + 2][startCell + 2].getState() == Space.State.EMPTY)
                    && (board[startRow + 1][startCell + 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow + 1][startCell + 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow + 1][startCell + 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
        }
        else if (board[startRow][startCell].getPiece().getColor() == Piece.PieceColor.RED) {
            if ((startRow + 2 == endRow) && (startCell - 2 == endCell) && (board[startRow + 2][startCell - 2].getState() == Space.State.EMPTY)
                    && (board[startRow + 1][startCell - 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow + 1][startCell - 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(Piece.Type.SINGLE, board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow + 1][startCell - 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow + 2 == endRow) && (startCell + 2 == endCell) && (board[startRow + 2][startCell + 2].getState() == Space.State.EMPTY)
                    && (board[startRow + 1][startCell + 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow + 1][startCell + 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(Piece.Type.SINGLE, board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow + 1][startCell + 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
        }
        else {
            if ((startRow - 2 == endRow) && (startCell - 2 == endCell) && (board[startRow - 2][startCell - 2].getState() == Space.State.EMPTY)
                    && (board[startRow - 1][startCell - 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow - 1][startCell - 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow - 1][startCell - 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow - 2 == endRow) && (startCell + 2 == endCell) && (board[startRow - 2][startCell + 2].getState() == Space.State.EMPTY)
                    && (board[startRow - 1][startCell + 1].getState() == Space.State.NOT_EMPTY)
                    && (board[startRow][startCell].getPiece().getColor() != board[startRow - 1][startCell + 1].getPiece().getColor())) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();

                    board[startRow - 1][startCell + 1].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This function will validate an attempted simple {@Move}
     *
     * @param move
     *      A {@link Move} object containing the starting and ending coordinates of a move
     * @return Boolean
     */
    public boolean validateSimpleMove(Move move, Space[][] board, boolean commitMove) {

        int startRow = move.getStart().getRow();
        int startCell = move.getStart().getCell();
        int endRow = move.getEnd().getRow();
        int endCell = move.getEnd().getCell();
        if (board[startRow][startCell].getPiece().getType() == Piece.Type.KING) {
            if ((startRow - 1 == endRow) && (startCell - 1 == endCell) && (board[startRow - 1][startCell - 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow - 1 == endRow) && (startCell + 1 == endCell) && (board[startRow - 1][startCell + 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow + 1 == endRow) && (startCell - 1 == endCell) && (board[startRow + 1][startCell - 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow + 1 == endRow) && (startCell + 1 == endCell) && (board[startRow + 1][startCell + 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
        }
        else if (board[startRow][startCell].getPiece().getColor() == Piece.PieceColor.RED) {
            if ((startRow + 1 == endRow) && (startCell - 1 == endCell) && (board[startRow + 1][startCell - 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow + 1 == endRow) && (startCell + 1 == endCell) && (board[startRow + 1][startCell + 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
        }
        else {
            if ((startRow - 1 == endRow) && (startCell - 1 == endCell) && (board[startRow - 1][startCell - 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
            else if ((startRow - 1 == endRow) && (startCell + 1 == endCell) && (board[startRow - 1][startCell + 1].getState() == Space.State.EMPTY)) {
                if (commitMove == true) {
                    board[endRow][endCell].addPiece(new Piece(board[startRow][startCell].getPiece().getType(), board[startRow][startCell].getPiece().getColor()));
                    board[startRow][startCell].removePiece();
                    addToPendingMoves(move);
                    addToPendingMoveBoards(board);
                }
                return true;
            }
        }
        return false;
    }

    /**
     * This function will iterate through the board until it finds a potential jump {@link Move}
     *
     * @param activePlayerColor  The active player's color
     * @return  true if there is a possible jump move, and false, otherwise
     */
    public boolean searchForJumps(Piece.PieceColor activePlayerColor, Space[][] board) {
        Move potentialJump;

        for (int x = 0; x < MAX_NUM_ROWS; x++){
            for (int y = 0; y < MAX_NUM_COLS; y++){
                if (board[x][y].getPiece() != null) {
                    if (board[x][y].getPiece().getColor() == activePlayerColor){
                        if (board[x][y].getPiece().getType() == Piece.Type.KING) {
                            if ((x - 2 >= 0) && (y - 2 >= 0)) {
                                potentialJump = new Move(new Position(x, y), new Position(x - 2, y - 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                            if ((x - 2 >= 0) && (y + 2 < MAX_NUM_COLS)) {
                                potentialJump = new Move(new Position(x, y), new Position(x - 2, y + 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                            if ((x + 2 < MAX_NUM_ROWS) && (y - 2 >= 0)) {
                                potentialJump = new Move(new Position(x, y), new Position(x + 2, y - 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                            if ((x + 2 < MAX_NUM_ROWS) && (y + 2 < MAX_NUM_COLS)) {
                                potentialJump = new Move(new Position(x, y), new Position(x + 2, y + 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                        }
                        else if(board[x][y].getPiece().getColor() == Piece.PieceColor.RED) {
                            if ((x + 2 < MAX_NUM_ROWS) && (y - 2 >= 0)) {
                                potentialJump = new Move(new Position(x, y), new Position(x + 2, y - 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                            if ((x + 2 < MAX_NUM_ROWS) && (y + 2 < MAX_NUM_COLS)) {
                                potentialJump = new Move(new Position(x, y), new Position(x + 2, y + 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                        }
                        else {
                            if ((x - 2 >= 0) && (y - 2 >= 0)) {
                                potentialJump = new Move(new Position(x, y), new Position(x - 2, y - 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                            if ((x - 2 >= 0) && (y + 2 < MAX_NUM_COLS)) {
                                potentialJump = new Move(new Position(x, y), new Position(x - 2, y + 2));
                                if (validateJumpMove(potentialJump, board, false)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This function iterates through the current board until it finds a potential jump move, from the current piece being moved
     * 
     * @param move the current move being made
     * @param board the active board
     * @param activePlayerColor the active player's color
     * @return true if move found, false otherwise
     */
    public boolean searchForJumpsForOnePiece(Move move, Space[][] board, Piece.PieceColor activePlayerColor) {
        Move potentialJump;

        //A move is created using the end position of the previous move as the starting position of the potential move
        int startRow = move.getEnd().getRow();
        int startCell = move.getEnd().getCell();
        int endRow;
        int endCell;
        if (board[startRow][startCell].getPiece().getType() == Piece.Type.KING) {
            endRow = startRow - 2;
            endCell = startCell - 2;
            if ((endRow >= 0) && (endCell >= 0)) {
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
            endRow = startRow - 2;
            endCell = startCell + 2;
            if ((endRow >= 0) && (endCell < MAX_NUM_COLS)) {
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
            endRow = startRow + 2;
            endCell = startCell - 2;
            if ((endRow < MAX_NUM_ROWS) && (endCell >= 0)) {
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
            endRow = startRow + 2;
            endCell = startCell + 2;
            if ((endRow < MAX_NUM_ROWS) && (endCell < MAX_NUM_COLS)) {
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
        }
        else if (activePlayerColor == Piece.PieceColor.RED) {
            endRow = startRow + 2;
            endCell = startCell - 2;
            if ((endRow < MAX_NUM_ROWS) && (endCell >= 0)) {
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
            endRow = startRow + 2;
            endCell = startCell + 2;
            if ((endRow < MAX_NUM_ROWS) && (endCell < MAX_NUM_COLS)) {
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
        }
        else {
            endRow = startRow - 2;
            endCell = startCell - 2;
            if ((endRow >= 0) && (endCell >= 0)) {
                endRow = startRow - 2;
                endCell = startCell - 2;
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
            endRow = startRow - 2;
            endCell = startCell + 2;
            if ((endRow >= 0) && (endCell < MAX_NUM_COLS)) {
                endRow = startRow - 2;
                endCell = startCell + 2;
                potentialJump = new Move(new Position(startRow, startCell), new Position(endRow, endCell));
                if (validateJumpMove(potentialJump, board, false)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * This function will iterate through the board until it finds a potential simple {@link Move}
     * @param activePlayerColor  The active player's color
     * @return  true if there is a possible simple move, and false, otherwise
     */
    public boolean searchForSimples(Piece.PieceColor activePlayerColor, Space[][] board) {

        Move potentialSimple;
        for (int x = 0; x < MAX_NUM_ROWS; x++) {
            for (int y = 0; y < MAX_NUM_COLS; y++) {
                if (board[x][y].getPiece() != null) {
                    if (board[x][y].getPiece().getColor() == activePlayerColor) {
                        if (board[x][y].getPiece().getType() == Piece.Type.KING) {
                            if ((x - 1 >= 0) && (y - 1 >= 0)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x - 1, y - 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                            if ((x - 1 >= 0) && (y + 1 < MAX_NUM_COLS)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x - 1, y + 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                            if ((x + 1 < MAX_NUM_ROWS) && (y - 1 >= 0)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x + 1, y - 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                            if ((x + 1 < MAX_NUM_ROWS) && (y + 1 < MAX_NUM_COLS)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x + 1, y + 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                        } else if (board[x][y].getPiece().getColor() == Piece.PieceColor.RED) {
                            if ((x + 1 < MAX_NUM_ROWS) && (y - 1 >= 0)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x + 1, y - 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                            if ((x + 1 < MAX_NUM_ROWS) && (y + 1 < MAX_NUM_COLS)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x + 1, y + 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                        } else {
                            if ((x - 1 >= 0) && (y - 1 >= 0)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x - 1, y - 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                            if ((x - 1 >= 0) && (y + 1 < MAX_NUM_COLS)) {
                                potentialSimple = new Move(new Position(x, y), new Position(x - 1, y + 1));
                                if (validateSimpleMove(potentialSimple, board, false)) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * This function will create a copy of a board array with {@link Space} objects.
     *
     * @param board  a Space board array
     * @return  a copy of the Space board array inputted as a parameter
     */
    public Space[][] createBoardCopy(Space[][] board) {

        Space[][] boardCopy = new Space[MAX_NUM_ROWS][MAX_NUM_COLS];
        for (int row = 0; row < MAX_NUM_ROWS; row++) {
            for (int col = 0; col < MAX_NUM_COLS; col++) {
                boardCopy[row][col] = new Space(board[row][col]);
            }
        }
        return boardCopy;
    }

    /**
     * This function will add the end of a move in the form of a board array with {@link Space} objects
     * to the top of a {@link Stack} of moves pending turn submission.
     *
     * @param move  a Space board array of the end of a move
     */
    public void addToPendingMoves(Move move) {
        pendingMoves.push(move);
    }

    /**
     * This function will remove the end of a move in the form of a board array with {@link Space} objects
     * from the top of a {@link Stack} of moves pending turn submission.
     *
     * @return  Stack with pending turn submission moves in the form of Space board arrays
     */
    public Move removeFromPendingMoves() {
        return pendingMoves.pop();
    }

    /**
     * This function will check if the {@link Stack} of moves pending turn submission is not empty.
     *
     * @return  true if it is not empty, and false, otherwise
     */
    public boolean hasMoves() {
        return !pendingMoves.empty();
    }

    /**
     * This function will get the top of {@link Stack} to see the latest pending move
     * 
     * @return latest pending move
     */
    public Move getLatestPendingMove() {
        return pendingMoves.peek();
    }

    /**
     * This function will get a pending move from {@link Stack} at the specified index
     * 
     * @return specified pending move
     */
    public Move getPendingMove(int index) {
        return pendingMoves.elementAt(index);
    }

    /**
     * Returns the number of pending moves 
     * 
     * @return number of pending moves
     */
    public int getSizePendingMoves() {
        return pendingMoves.size();
    }

    /**
     * Clears all pending moves currently in the {@link Stack} 
     * 
     */
    public void clearPendingMoves() {
        pendingMoves.clear();
    }


    /**
     * This function will add the end of a move in the form of a board array with {@link Space} objects
     * to the top of a {@link Stack} of moves pending turn submission.
     *
     * @param board  a Space board array of the end of a move
     */
    public void addToPendingMoveBoards(Space[][] board) {
        pendingMoveBoards.push(board);
    }

    /**
     * This function will remove the end of a move in the form of a board array with {@link Space} objects
     * from the top of a {@link Stack} of moves pending turn submission.
     *
     * @return  Stack with pending turn submission moves in the form of Space board arrays
     */
    public Space[][] removeFromPendingMoveBoards() {
        return pendingMoveBoards.pop();
    }

    /**
     * This function will check if the {@link Stack} of moves pending turn submission is not empty.
     *
     * @return  true if it is not empty, and false, otherwise
     */
    public boolean hasBoards() {
        return !pendingMoveBoards.empty();
    }

    /**
     * Return the ending position of the last board
     * 
     * @return latest Space
     */
    public Space[][] getLatestPendingMoveEndPositionBoard() {
        return pendingMoveBoards.peek();
    }

    /**
     * Get the pending move board at a specified index
     * @param index
     * @return Space at the specified index
     */
    public Space[][] getPendingMoveBoard(int index) {
        return pendingMoveBoards.elementAt(index);
    }

    /**
     * Remove all Pending Move Boards from {@link Stack}
     */
    public void clearPendingMoveBoards() {
        pendingMoveBoards.clear();
    }

}
