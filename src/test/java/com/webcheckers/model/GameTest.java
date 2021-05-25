package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import com.webcheckers.util.Message;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Testing class for GameLogic
 *
 * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
 */
@Tag("Model-tier")
public class GameTest {

    private static final Message DISTANT_MOVE = new Message("You cannot move that far.", Message.Type.ERROR);
    private static final Message SIMPLE_MOVE = new Message("You moved a piece.", Message.Type.INFO);
    private static final Message JUMP_MOVE = new Message("You captured a piece.", Message.Type.INFO);

    private Game CuT;
    private Player player1;
    private Player player2;

    @BeforeEach
    public void setup(){
        player1 = mock(Player.class);
        player2 = mock(Player.class);

        when(player1.getName()).thenReturn("player1");
        when(player2.getName()).thenReturn("player2");

        CuT = new Game(1, player1, player2);
    }

    /**
     * Test if games are properly ended after a player leaves the game
     */
    @Test
    public void testGameAbandonLogic() {
        // Check if game is active
        Assertions.assertTrue(CuT.isActive());
        Assertions.assertNull(CuT.getGameEndedReason());

        boolean ret = CuT.endGame(Game.Status.RED_ABANDONED);
        Message returnMessage = CuT.getGameEndedReason();

        // Check that the game is no longer after is was abandoned
        Assertions.assertTrue(ret);
        Assertions.assertFalse(CuT.isActive());

        ret = CuT.endGame(Game.Status.WHITE_ABANDONED);

        // After attempting to end the game again make sure it fails and keeps old status
        Assertions.assertFalse(ret);
        Assertions.assertFalse(CuT.isActive());
        Assertions.assertEquals(CuT.getGameEndedReason(), returnMessage);
    }

    /**
     * Test proper player assignment
     */
    @Test
    public void testPlayerAssignment() {
        Assertions.assertEquals(CuT.getPlayers()[0], player1);
        Assertions.assertEquals(CuT.getPlayers()[1], player2);
    }

    /**
     * Test proper game ID assignment
     */
    @Test
    public void testGameIDAssignment() {
        Assertions.assertEquals(CuT.getGameID(), 1);
    }

    /**
     * Test promotion of a single piece to a king piece
     */
    @Test
    public void testPromotion() {
        Space[][] board = CuT.getBoard();

        // Promoting a piece in the first and last row of the board
        CuT.kingMe(0, 1, board);
        CuT.kingMe(7, 0, board);

        board = CuT.getBoard();

        // Checking that the pieces were promoted
        Assertions.assertEquals(board[0][1].getPiece().getType(), Piece.Type.KING);
        Assertions.assertEquals(board[0][1].getPiece().getType(), Piece.Type.KING);
    }

    /**
     * Test moving a piece more spaces than allowed
     */
    @Test
    public void testMoveTooFar() {
        Space[][] board = CuT.getBoard();
        Move distantMove = new Move(new Position(0, 0), new Position(5, 4));
        Message returnMessage = CuT.validateMove(distantMove, board);

        Assertions.assertEquals(returnMessage, DISTANT_MOVE);
    }

    /**
     * Test simple piece movements
     */
    @Test
    public void testSimpleMove() {
        Space[][] board = CuT.getBoard();

        // Create new move for a red piece
        Move simpleMove = new Move(new Position(2, 1), new Position(3, 0));

        // Attempt simple move
        Message returnMessage = CuT.validateMove(simpleMove, board);

        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        simpleMove = new Move(new Position(1, 0), new Position(2, 1));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        // Create new move for a white piece
        simpleMove = new Move(new Position(5, 0), new Position(4, 1));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        simpleMove = new Move(new Position(6, 1), new Position(5, 0));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        // Create new move for a king piece
        board[5][6].getPiece().modifyTypeToKING();
        simpleMove = new Move(new Position(5, 6), new Position(4, 5));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        simpleMove = new Move(new Position(4, 5), new Position(5, 6));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        simpleMove = new Move(new Position(5, 6), new Position(4, 7));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);

        simpleMove = new Move(new Position(4, 7), new Position(5, 6));
        returnMessage = CuT.validateMove(simpleMove, board);
        Assertions.assertEquals(returnMessage, SIMPLE_MOVE);
    }

    /**
     * Test jump moves
     */
    @Test
    public void testJumpMove() {
        Space[][] board = CuT.getBoard();

        // Set up jump moves for red pieces
        board[3][2].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        board = CuT.getBoard();

        // Create new move for a red piece
        Move jumpMove = new Move(new Position(2, 1), new Position(4, 3));

        // Attempting jump move
        Message returnMessage = CuT.validateMove(jumpMove, board);

        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        board[3][2].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));
        board = CuT.getBoard();
        jumpMove = new Move(new Position(2, 3), new Position(4, 1));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        // Set up jump moves for white pieces
        board = CuT.getBoard();
        jumpMove = new Move(new Position(5, 0), new Position(3, 2));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        board = CuT.getBoard();
        board[3][2].removePiece();
        board = CuT.getBoard();
        jumpMove = new Move(new Position(5, 4), new Position(3, 2));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        // Set up jump moves for king pieces
        board = CuT.getBoard();
        board[2][3].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.RED));
        board = CuT.getBoard();
        jumpMove = new Move(new Position(2, 3), new Position(4, 1));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        board = CuT.getBoard();
        board[3][2].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));
        board = CuT.getBoard();
        jumpMove = new Move(new Position(4, 1), new Position(2, 3));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        board = CuT.getBoard();
        board[3][4].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));
        board = CuT.getBoard();
        jumpMove = new Move(new Position(2, 3), new Position(4, 5));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);

        board = CuT.getBoard();
        board[3][4].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));
        board = CuT.getBoard();
        jumpMove = new Move(new Position(4, 5), new Position(2, 3));
        returnMessage = CuT.validateMove(jumpMove, board);
        Assertions.assertEquals(returnMessage, JUMP_MOVE);
    }

    /**
     * Test searching for all potential simple moves
     */
    @Test
    public void testSearchForSimples() {
        Space[][] board = CuT.getBoard();

        // Search for simple moves for red and white players
        Assertions.assertTrue(CuT.searchForSimples(Piece.PieceColor.RED, board));
        Assertions.assertTrue(CuT.searchForSimples(Piece.PieceColor.WHITE, board));

        // Set up king piece scenarios to search for simples
        board[1][2].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForSimples(Piece.PieceColor.WHITE, board));

        board[1][2].removePiece();
        board[1][0].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForSimples(Piece.PieceColor.WHITE, board));

        board[1][0].removePiece();
        board[0][1].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForSimples(Piece.PieceColor.WHITE, board));

        board[0][1].removePiece();
        board[0][0].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForSimples(Piece.PieceColor.WHITE, board));
    }

    /**
     * Test searching for all potential jump moves
     */
    @Test
    public void testSearchForJumps() {
        Space[][] board = CuT.getBoard();

        // Search for jump moves for red and white players
        Assertions.assertFalse(CuT.searchForJumps(Piece.PieceColor.RED, board));
        Assertions.assertFalse(CuT.searchForJumps(Piece.PieceColor.WHITE, board));

        // Create a red piece to set up a jump move for a white piece
        board[4][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        Assertions.assertTrue(CuT.searchForJumps(Piece.PieceColor.WHITE, board));

        // Set up king piece scenarios to search for jumps
        board[2][3].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForJumps(Piece.PieceColor.WHITE, board));

        board[2][3].removePiece();
        board[2][1].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForJumps(Piece.PieceColor.WHITE, board));

        board[2][1].removePiece();
        board[1][2].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForJumps(Piece.PieceColor.WHITE, board));

        board[1][2].removePiece();
        board[1][0].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForJumps(Piece.PieceColor.WHITE, board));
    }

    /**
     * Test searching for potential jump moves for a single piece
     */
    @Test
    public void testSearchForJumpsForOnePiece() {
        //Note: For all scenarios, the starting position of the jump move is never checked. This method builds potential
        //jump moves off of the end position of the Move object being passed. For this reason, all starting positions
        //for the jump moves created here are set to 0, 0 for simplicity sake.

        Space[][] board = CuT.getBoard();

        // Creating a scenario to set up a jump move for a white piece
        board[4][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        // Testing jump scenarios
        Move potentialMove = new Move(new Position(0, 0), new Position(5, 2));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.WHITE));

        potentialMove = new Move(new Position(0, 0), new Position(5, 4));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.WHITE));

        // Creating a scenario to set up a jump move for a red piece
        board[4][3].removePiece();
        board[3][2].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        // Testing jump scenarios
        potentialMove = new Move(new Position(0, 0), new Position(2, 1));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.RED));

        potentialMove = new Move(new Position(0, 0), new Position(2, 3));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.RED));

        // Creating a scenario to set up a jump move for a King piece
        board[3][2].removePiece();
        board[5][2].removePiece();
        board[5][4].removePiece();
        board[3][2].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.RED));
        board[3][4].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.RED));
        board[4][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.WHITE));

        // Set up king piece scenarios to search for jumps
        board[2][3].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        Assertions.assertTrue(CuT.searchForJumps(Piece.PieceColor.WHITE, board));

        // Testing jump scenarios
        potentialMove = new Move(new Position(0, 0), new Position(3, 2));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.RED));

        potentialMove = new Move(new Position(0, 0), new Position(3, 4));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.RED));

        // Set up more king piece scenarios to search for jumps
        board[3][2].removePiece();
        board[3][4].removePiece();
        board[4][3].removePiece();
        board[5][2].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        board[5][4].addPiece(new Piece(Piece.Type.KING, Piece.PieceColor.WHITE));
        board[4][3].addPiece(new Piece(Piece.Type.SINGLE, Piece.PieceColor.RED));

        // Testing jump scenarios
        potentialMove = new Move(new Position(0, 0), new Position(5, 2));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.WHITE));

        potentialMove = new Move(new Position(0, 0), new Position(5, 4));
        Assertions.assertTrue(CuT.searchForJumpsForOnePiece(potentialMove, board, Piece.PieceColor.WHITE));

    }
}
