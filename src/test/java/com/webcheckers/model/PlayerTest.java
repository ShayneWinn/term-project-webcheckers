package com.webcheckers.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

/**
 * Unit Testing for player.java
 *
 * @author <a href='mailto:spm8848@rit.edu'>Sean McDonnell</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
@Tag("Model-tier")
public class PlayerTest {

    private static final String PLAYER1_NAME = "player 1";
    private static final String PLAYER2_NAME = "player 2";
    private static final String PLAYER3_NAME = "player 3";
    private static Player testPlayer1 = new Player(PLAYER1_NAME);
    private static Player testPlayer2 = new Player(PLAYER2_NAME);
    private static Player testPlayer3 = new Player(PLAYER3_NAME);
    private Game testGame;

    @BeforeEach
    public void setup(){
        testGame = new Game(1, testPlayer1, testPlayer2);
    }
     /**
     * Test getting a player's name
     */
    @Test
    public void testPlayerName() {
        Assertions.assertEquals(PLAYER1_NAME, testPlayer1.getName());
    }

    /**
     * Test if a player is in a game
     */
    @Test
    public void testInGame() {
        // Test while a player is in the game 
        testPlayer1.joinGame(testGame);
        Assertions.assertTrue(testPlayer1.inGame());

        // Test if a player is not in a game 
        testPlayer1.leaveGame();
        Assertions.assertFalse(testPlayer1.inGame());
    }

    /**
     * Test if correct game is gotten 
     */
    @Test
    public void testCurrentGame() {
        testPlayer1.joinGame(testGame);
        Assertions.assertEquals(testPlayer1.getGame(), testGame);
    }

    /**
     * Test if player is in active game
     */
    @Test
    public void testActiveGame() {
        testPlayer1.joinGame(testGame);
        Assertions.assertTrue(testPlayer1.isGameActive());

        // End Game
        testPlayer1.leaveGame();
        Assertions.assertFalse(testPlayer1.isGameActive());
    }

    /**
     * Test the turn changing as a spectator
     */
    @Test
    public void testSpectateDidTurnUpdate() {

        // Start spectating the game
        testPlayer3.spectateGame(testGame);
        Assertions.assertFalse(testPlayer3.spectateDidTurnUpdate());

        //Changing the turn
        testGame.changeTurn();

        Assertions.assertTrue(testPlayer3.spectateDidTurnUpdate());
    }


}
