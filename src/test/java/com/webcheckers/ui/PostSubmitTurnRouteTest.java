package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import spark.*;
import java.util.HashMap;
import java.util.Map;
import com.webcheckers.util.Message;

import com.webcheckers.appl.PlayerLobby;


/**
 * Unit testing of PostSubmitTurnRoute functionality
 *
 *  @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("UI-tier")
public class PostSubmitTurnRouteTest {

    private static final String CURRENT_USER_USERNAME = "CURRENT USER";
    private static final String OPPONENT_USERNAME = "OPPONENT";
    private static final String STRING_CONTAINS_NULL = "null";
    private static final String EMPTY_STRING = "";
    private static final String SIMPLE_MOVE_JSON = "{\"start\":{\"row\":2,\"cell\":7},\"end\":{\"row\":3,\"cell\":6}}";
    private static final String VALID_SIMPLE_MOVE_MESSAGE = "You moved a piece.";
    /**
     * The component-under-test (CuT)
     */
    private PostSubmitTurnRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerLobby;
    private Player currentUser;
    private Player opponent;
    private Game currentUserGame;
    private Space[][] board;
    private Space[][] initialBoardCopy;
    private Space[][] boardCopy;
    private Message expectedReturnMessage;
    private Object messageRouteReturns;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();
        currentUser = new Player(CURRENT_USER_USERNAME);
        when(session.attribute(PostSubmitTurnRoute.CURRENT_USER_ATTR)).thenReturn(currentUser);
        opponent = new Player(OPPONENT_USERNAME);

        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        currentUserGame = currentUser.getGame();
        board = currentUserGame.getBoard();

        CuT = new PostSubmitTurnRoute(playerLobby);
    }

    /**
     * Test proper return of jump move available error when the corresponding situation occurs
     */
    @Test
    public void test_jump_move_available_error() {
        board[0][5].removePiece();
        board[2][3].removePiece();
        board[2][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[5][0].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(5, 0), new Position(4, 1)));

        expectedReturnMessage = Message.error(PostSubmitTurnRoute.JUMP_MOVE_AVAIL_MSG);
        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper return of one single move only error when the corresponding situation occurs
     */
    @Test
    public void test_one_single_move_only_error() {
        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[5][0].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(5, 0), new Position(4, 1)));

        board[5][2].removePiece();
        board[4][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(5, 2), new Position(4, 3)));

        expectedReturnMessage = Message.error(PostSubmitTurnRoute.ONE_SINGLE_MOVE_ONLY_MSG);
        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper return of jump moves only error when the corresponding situation occurs
     */
    @Test
    public void test_jump_moves_only_error() {
        board[5][2].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        board[2][3].removePiece();
        board[3][2].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[4][1].removePiece();
        board[2][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        board[3][2].removePiece();

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(4, 1), new Position(2, 3)));

        board[5][0].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(5, 0), new Position(4, 1)));

        expectedReturnMessage = Message.error(PostSubmitTurnRoute.JUMP_MOVES_ONLY_MSG);
        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper return of partial jump move error when the corresponding situation occurs
     */
    @Test
    public void test_partial_jump_move_error() {
        board[5][0].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        board[2][3].removePiece();
        board[3][2].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        board[0][5].removePiece();

        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[4][1].removePiece();
        board[2][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        board[3][2].removePiece();

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(4, 1), new Position(2, 3)));

        expectedReturnMessage = Message.error(PostSubmitTurnRoute.JUMP_MOVE_PARTIAL_MSG);
        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper return of valid turn for a White player
     */
    @Test
    public void test_valid_turn_for_White_player() {
        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[5][0].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(5, 0), new Position(4, 1)));

        expectedReturnMessage = Message.info(PostSubmitTurnRoute.VALID_TURN);
        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper return of valid turn for a Red player
     */
    @Test
    public void test_valid_turn_for_Red_player() {
        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[2][1].removePiece();
        board[3][0].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(2, 1), new Position(3, 0)));

        expectedReturnMessage = Message.info(PostSubmitTurnRoute.VALID_TURN);
        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper kingMe utilization during corresponding scenario
     */
    @Test
    public void test_kingMe_scenario() {
        board[0][5].removePiece();
        board[2][3].removePiece();

        board[5][0].removePiece();
        board[2][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[2][3].removePiece();
        board[0][5].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        board[1][4].removePiece();

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(2, 3), new Position(0, 5)));

        CuT.handle(request, response);

        Assertions.assertEquals(Piece.Type.KING, currentUser.getGame().getBoard()[0][5].getPiece().getType());
    }

    /**
     * Test proper return of valid turn for a White player
     */
    @Test
    public void test_proper_automatic_resignation_after_white_wins() {
        board[0][1].removePiece();
        board[0][3].removePiece();
        board[0][5].removePiece();
        board[0][7].removePiece();
        board[1][0].removePiece();
        board[1][2].removePiece();
        board[1][4].removePiece();
        board[1][6].removePiece();
        board[2][1].removePiece();
        board[2][3].removePiece();
        board[2][5].removePiece();
        board[2][7].removePiece();

        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[5][0].removePiece();
        board[4][1].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(5, 0), new Position(4, 1)));

        CuT.handle(request, response);

        Assertions.assertEquals(opponent.getName(), opponent.getGame().getPlayers()[1].getName());
    }

    /**
     * Test proper return of valid turn for a Red player
     */
    @Test
    public void test_proper_automatic_resignation_after_red_wins() {
        board[5][0].removePiece();
        board[5][2].removePiece();
        board[5][4].removePiece();
        board[5][6].removePiece();
        board[6][1].removePiece();
        board[6][3].removePiece();
        board[6][5].removePiece();
        board[6][7].removePiece();
        board[7][0].removePiece();
        board[7][2].removePiece();
        board[7][4].removePiece();
        board[7][6].removePiece();

        initialBoardCopy = currentUserGame.createBoardCopy(currentUserGame.getBoard());

        currentUserGame.addToPendingMoveBoards(initialBoardCopy);

        board[2][1].removePiece();
        board[3][0].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        boardCopy = currentUserGame.createBoardCopy(board);

        currentUserGame.addToPendingMoveBoards(boardCopy);
        currentUserGame.addToPendingMoves(new Move(new Position(2, 1), new Position(3, 0)));

        CuT.handle(request, response);

        Assertions.assertEquals(currentUser.getName(), currentUser.getGame().getPlayers()[0].getName());
    }

}


