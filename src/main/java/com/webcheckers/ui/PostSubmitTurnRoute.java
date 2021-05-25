package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.*;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.util.Message;

/**
 * the {@code POST /submitTurn} route handler.
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */

public class PostSubmitTurnRoute implements Route {

    /**
     * Attributes
     *
     */
    private static final int MAX_ROW_INDEX = 7;

    private static final Logger LOG = Logger.getLogger(PostSubmitTurnRoute.class.getName());

    static final String CURRENT_USER_ATTR = "currentUser";
    static final String VALID_TURN = "You submitted a valid turn.";
    static final String JUMP_MOVE_AVAIL_MSG   = "A jump move is available. You must take it.";
    static final String JUMP_MOVE_PARTIAL_MSG = "You must continue the jump move.";
    static final String JUMP_MOVES_ONLY_MSG    = "You can only make jumps during a jump move.";
    static final String ONE_SINGLE_MOVE_ONLY_MSG  = "You can only make one single move in a turn.";

    private final PlayerLobby playerLobby;

    private int numberOfSimples = 0;
    private int numberOfJumps = 0;

    /**
     * Constructor to create a {@link PostSubmitTurnRoute}
     *
     * @param playerLobby  {@link PlayerLobby} object for extraction and manipulation of {@link Game} information
     */
    public PostSubmitTurnRoute(PlayerLobby playerLobby) {
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");

        LOG.config("PostSubmitTurnRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSubmitTurnRoute invoked");

        final Session httpSession = request.session();

        Player activePlayer = httpSession.attribute(CURRENT_USER_ATTR);
        Game game = activePlayer.getGame();
        Space[][] actualBoardCopy = game.createBoardCopy(game.getBoard());
        Message returnMessage;

        int numberOfPendingMoves = game.getSizePendingMoves();

        for (int i = 0; i < numberOfPendingMoves; i++) {
            Move move = game.getPendingMove(i);
            Space[][] board;

            board = game.getPendingMoveBoard(i);

            if (game.validateSimpleMove(move, board, false)) {
                numberOfSimples++;
            } else if (game.validateJumpMove(move, board, false)) {
                numberOfJumps++;
            }
        }

        Move latestPendingMove = game.getLatestPendingMove();
        Space[][] board;
        Space[][] latestPendingMoveEndPositionBoard = game.getLatestPendingMoveEndPositionBoard();
        Piece.PieceColor activePlayerColor =
                latestPendingMoveEndPositionBoard[latestPendingMove.getEnd().getRow()]
                        [latestPendingMove.getEnd().getCell()].getPiece().getColor();

        if (numberOfSimples == 1 && numberOfJumps == 0) {
            board = actualBoardCopy;

            if (game.searchForJumps(activePlayerColor, board)) {
                numberOfSimples = 0;
                numberOfJumps = 0;
                return Message.error(JUMP_MOVE_AVAIL_MSG).toJson();
            }
        } else if (numberOfSimples > 1 && numberOfJumps == 0) {
            numberOfSimples = 0;
            numberOfJumps = 0;
            return Message.error(ONE_SINGLE_MOVE_ONLY_MSG).toJson();
        } else if (numberOfSimples > 0 && numberOfJumps > 0) {
            numberOfSimples = 0;
            numberOfJumps = 0;
            return Message.error(JUMP_MOVES_ONLY_MSG).toJson();
        } else if (numberOfSimples == 0 && numberOfJumps > 0) {
            if (game.searchForJumpsForOnePiece(latestPendingMove,
                    latestPendingMoveEndPositionBoard, activePlayerColor)) {
                numberOfSimples = 0;
                numberOfJumps = 0;
                return Message.error(JUMP_MOVE_PARTIAL_MSG).toJson();
            }
        }

        if(latestPendingMove.getEnd().getRow() == 0 || latestPendingMove.getEnd().getRow() == MAX_ROW_INDEX){
            game.kingMe(latestPendingMove.getEnd().getRow(), latestPendingMove.getEnd().getCell(),
                    latestPendingMoveEndPositionBoard);
        }

        game.modifyActualBoard(latestPendingMoveEndPositionBoard);

        returnMessage = Message.info(VALID_TURN);

        numberOfSimples = 0;
        numberOfJumps = 0;

        if(activePlayerColor == Piece.PieceColor.RED) {
            if (game.searchForJumps(Piece.PieceColor.WHITE, game.getBoard()) == false && game.searchForSimples(Piece.PieceColor.WHITE, game.getBoard()) == false) {
                //Resigns the white player
                playerLobby.resignGame(game.getPlayers()[1]);
            }
            //changeTurn() is inside else statement to prevent double calls from resignGame() which also contains changeTurn()
            else {
                game.changeTurn();
            }
        }
        else{
            if (game.searchForJumps(Piece.PieceColor.RED, game.getBoard()) == false && game.searchForSimples(Piece.PieceColor.RED, game.getBoard()) == false) {
                //Resigns the red player
                playerLobby.resignGame(game.getPlayers()[0]);
            }
            //changeTurn() is inside else statement to prevent double calls from resignGame() which also contains changeTurn()
            else {
                game.changeTurn();
            }
        }
        return returnMessage.toJson();
    }

}


