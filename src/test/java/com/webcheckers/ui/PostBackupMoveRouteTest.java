package com.webcheckers.ui;


import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;

import com.webcheckers.model.*;
import com.webcheckers.util.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Session;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Unit testing of PostBackupMoveRoute functionality
 *
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 */
@Tag("UI-tier")
public class PostBackupMoveRouteTest {

    private static final String CURRENT_USER = "currentUser";
    private static final String BACKUP_MESSAGE = "You moved a piece back.";
    /**
     * CuT: PostBackupMoveRoute
     */
    private PostBackupMoveRoute CuT;

    /**
     * Mock objects
     */
    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerLobby;
    private Game testGame;
    private Move testMove;
    private Space[][] board;
    private Player testUser1, testUser2;

    @BeforeEach
    public void setUp() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();
        //create a game
        testUser1 = new Player("testUser1");
        testUser2 = new Player("testUser2");
        playerLobby.signIn(testUser1);
        playerLobby.signIn(testUser2);
        playerLobby.startGame(testUser1, testUser2.getName());
        testGame = testUser1.getGame();
        board = testGame.getBoard();
        //creates a pending move


        CuT = new PostBackupMoveRoute(playerLobby);
    }

    /**
     * Tests that backup move pops pendingMoves stack correctly.
     */
    @Test
    public void test_single_backup() {
        //expected message
        Message expectedReturnMessage = Message.info(BACKUP_MESSAGE);

        testMove = new Move(new Position(2, 1), new Position(3, 2));

        Space[][] boardCopy = testGame.createBoardCopy(board);
        testGame.addToPendingMoveBoards(board);

        when(session.attribute(CURRENT_USER)).thenReturn(testUser1);
        //no pending moves

        Assertions.assertEquals(0, testGame.getSizePendingMoves());
        //1 pending move
        testGame.validateMove(testMove, boardCopy);
        Assertions.assertEquals(1, testGame.getSizePendingMoves());

        //start test
        Object returnMessage = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), returnMessage);
    }

    /**
     * Test multiple backup moves
     */
    @Test
    public void test_multiple_backup() {
        //expected message
        Message expectedReturnMessage = Message.info(BACKUP_MESSAGE);

        testMove = new Move(new Position(2, 1), new Position(4, 3));

        //adding pieces to jump
        board[3][2].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        board[5][2].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        board[6][2].removePiece();
        Space[][] boardCopy = testGame.createBoardCopy(board);
        testGame.addToPendingMoveBoards(board);

        when(session.attribute(CURRENT_USER)).thenReturn(testUser1);
        //no pending moves

        Assertions.assertEquals(0, testGame.getSizePendingMoves());
        //1 pending move
        testGame.validateMove(testMove, boardCopy);
        Assertions.assertEquals(1, testGame.getSizePendingMoves());

        //2 pending moves
        Move testMove2 = new Move(new Position(4, 3), new Position(6, 2));
        testGame.validateMove(testMove2, boardCopy);


        Object returnMessage = CuT.handle(request, response);
        Assertions.assertEquals(expectedReturnMessage.toJson(), returnMessage);
        Assertions.assertEquals(expectedReturnMessage.toJson(), returnMessage);

    }
}
