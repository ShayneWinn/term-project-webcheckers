package com.webcheckers.appl;

import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collection;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * A class to test functionality of {@linkplain PlayerLobby} PlayerLobbies
 * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn </a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
@Tag("Appl-tier")
public class PlayerLobbyTest {

    private static final Message SPECTATE_NOT_IN_GAME = new Message("This player is no longer in a game!", Message.Type.ERROR);

    /**
     * Component Under Test
     */
    private PlayerLobby CuT;

    private Player player1;
    private Player player2;
    private Player player3;
    private Player player4;

    private String player1Name = "player1";
    private String player2Name = "player2";
    private String player3Name = "player3";
    private String player4Name = "player4";

    /**
     * Setup, create CuT and create players
     */
    @BeforeEach
    public void setup() {
        CuT = new PlayerLobby();
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        player3 = new Player(player3Name);
        player4 = new Player(player4Name);
    }


    /**
     * Test that a player can successfully sign in
     */
    @Test 
    public void testValidPlayerNames() {
        player1 = new Player("TestString");
        player2 = new Player("test with spaces");
        player3 = new Player("t35t with 5pac3s and numb3r5");

        // Test username with no spaces
        Message ret = CuT.signIn(player1);
        assertNull(ret);

        // Test username with spaces
        ret = CuT.signIn(player2);
        assertNull(ret);

        // Test username with spaces and numbers
        ret = CuT.signIn(player3);
        assertNull(ret);
    }

    /**
     * Test that a message is properly returned when a username is taken
     */
    @Test
    public void testTakenUsername() {
        Player playerCopy = mock(Player.class);
        when(playerCopy.getName()).thenReturn(player1Name);

        Message ret = CuT.signIn(player1);
        assertNull(ret);

        ret = CuT.signIn(playerCopy);

        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
    }

    /**
     * Check that a message is returned when a username is invalid
     */
    @Test
    public void testInvalidPlayerNames() {
        Message ret;
        Player invalidPlayer = mock(Player.class);

        // Test username with only whitespace
        when(invalidPlayer.getName()).thenReturn("    ");
        ret = CuT.signIn(invalidPlayer);
        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertNull(CuT.findPlayer("    "));

        // Test username with non alpha-numerical
        when(invalidPlayer.getName()).thenReturn("*");
        ret = CuT.signIn(invalidPlayer);
        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertNull(CuT.findPlayer("*"));

        // Test username with non alpha-numeric and whitespace
        when(invalidPlayer.getName()).thenReturn("*!  /");
        ret = CuT.signIn(invalidPlayer);
        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertNull(CuT.findPlayer("*!  /"));
    }


    /**
     * Test that player can sign out
     */
    @Test
    public void testPlayerSignOut() {
        CuT.signIn(player1);

        // Check if player is currently signed in
        Player playerReturn = CuT.findPlayer(player1Name);
        assertEquals(player1, playerReturn);

        // Sign out player1
        boolean ret = CuT.signOut(player1);
        playerReturn = CuT.findPlayer(player1Name);
        assertTrue(ret);
        assertNull(playerReturn);
    }

    /**
     * Test invalid player's ability to sign out
     */
    @Test
    public void testInvalidPlayerSignOut() {
        // Make sure they are not signed in
        assertNull(CuT.findPlayer((player1Name)));

        // Sign out invalid player
        boolean ret = CuT.signOut(player1);
        assertFalse(ret);

        // Check if they are still not signed in
        assertNull(CuT.findPlayer((player1Name)));
    }

    /**
     * Test player sign out while in a game 
     */
    @Test
    public void testPlayerSignOutInGame() {
        CuT.signIn(player1);
        CuT.signIn(player2);
        CuT.startGame(player1, player2.getName());

        assertTrue(player1.inGame());
        assertTrue(player2.inGame());
        assertTrue(player1.isGameActive());
        assertTrue(player2.isGameActive());
        assertEquals(player1.getGame(), player2.getGame());

        // Only change if the game is active
        CuT.signOut(player1);
        assertTrue(player1.inGame());
        assertTrue(player2.inGame());
        assertFalse(player1.isGameActive());
        assertFalse(player2.isGameActive());
        assertEquals(player1.getGame(), player2.getGame());
    }


    /**
     * Test starting a game between two players
     */
    @Test
    public void testStartGame() {
        CuT.signIn(player1);
        CuT.signIn(player2);
        Message ret;

        ret = CuT.startGame(player1, player2Name);
        assertNull(ret);
        assertTrue(player1.inGame());
        assertTrue(player2.inGame());
        assertEquals(player1.getGame(), player2.getGame());
    }

    /**
     * Test starting a game between two players with invalid user
     */
    @Test
    public void testStartGameInvalidUser() {
        CuT.signIn(player2);
        Message ret;

        ret = CuT.startGame(player1, player2Name);
        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertFalse(player1.inGame());
    }

    /**
     * Test starting a game between two players with invalid opponent
     */
    @Test
    public void testStartGameInvalidOpponent() {
        CuT.signIn(player1);
        Message ret;

        ret = CuT.startGame(player1, "invalid name");
        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertFalse(player1.inGame());
    }

    /**
     * Test starting a game with a player that is already in a game
     */
    @Test
    public void testStartGameBusyOpponent() {
        CuT.signIn(player1);
        CuT.signIn(player2);
        CuT.signIn(player3);
        Message ret;

        CuT.startGame(player1, player2.getName());
        ret = CuT.startGame(player3, player2Name);
        assertNotNull(ret);
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertFalse(player3.inGame());
        assertTrue(player1.inGame());
        assertTrue(player2.inGame());

    }

    /**
     * Test that the proper players are returned when attempting to get available players
     */
    @Test
    public void testAvailablePlayers() {
        CuT.signIn(player1);
        CuT.signIn(player2);
        CuT.signIn(player3);
        CuT.signIn(player4);

        // Create list of all players
        Collection<Player> availableTest = new ArrayList<>();
        availableTest.add(player1);
        availableTest.add(player2);
        availableTest.add(player3);
        availableTest.add(player4);

        // Test the list of players
        Collection<Player> availableReturn = new ArrayList<>(CuT.getAvailablePlayers());
        assertEquals(availableTest, availableReturn);

        // Sign one out and put other two in game
        CuT.signOut(player1);
        CuT.startGame(player2, player3.getName());

        // Remove player from the test list
        availableTest.remove(player1);
        availableTest.remove(player2);
        availableTest.remove(player3);

        // test if they were properly removed
        availableReturn = new ArrayList<>(CuT.getAvailablePlayers());
        assertEquals(availableTest, availableReturn);
    }


    /**
     * Test that the correct player count is returned
     */
    @Test
    public void testPlayerCount() {
        CuT.signIn(player1);
        CuT.signIn(player2);
        CuT.signIn(player3);
        CuT.signIn(player4);

        // Test the current list of players
        int ret = CuT.getPlayerCount();
        assertEquals(ret, 4);

        // Sign one out and put other two in game
        CuT.signOut(player1);
        CuT.startGame(player2, player3.getName());

        // Test if the players were properly removed
        ret = CuT.getPlayerCount();
        assertEquals(ret, 3);
    }


    /**
     * Test that the correct player is returned
     */
    @Test
    public void testFindPlayer() {
        CuT.signIn(player1);

        Player playerReturn = CuT.findPlayer(player1Name);
        assertEquals(playerReturn, player1);
    }

    /**
     * Test proper return for invalid name
     */
    @Test
    public void testFindPlayerInvalidName() {
        Player playerReturn = CuT.findPlayer("invalidName");
        assertNull(playerReturn);
    }


    /**
     * Test that proper usernames are valid/invalid
     */
    @Test
    public void testUsernameCheck() {
        boolean ret;

        // Valid Usernames
        // alpha
        ret = CuT.isValid("test");
        assertTrue(ret);
        // alpha-numeric
        ret = CuT.isValid("test1234");
        assertTrue(ret);
        // numeric
        ret = CuT.isValid("1234");
        assertTrue(ret);
        // alpha-numeric + spaces
        ret = CuT.isValid("test with 12345 and spaces");
        assertTrue(ret);

        // Invalid Usernames
        // only spaces
        ret = CuT.isValid("  ");
        assertFalse(ret);
        // invalid characters
        ret = CuT.isValid("test!");
        assertFalse(ret);
    }

    /**
     * Test that correct usernames are listed as available
     */
    @Test
    public void testListAvailableUsers() {
        CuT.signIn(player1);
        boolean ret;

        // username available
        ret = CuT.isAvailable("new name");
        assertTrue(ret);

        // username unavailable
        ret = CuT.isAvailable(player1Name);
        assertFalse(ret);
    }

    /**
     * Test spectating a game between two players
     */
    @Test
    public void testSpectateGame() {
        CuT.signIn(player1);
        CuT.signIn(player2);
        CuT.signIn(player3);
        CuT.signIn(player4);
        Message ret;

        // Tests amount of players available to play and spectate
        ret = CuT.startGame(player1, player2Name);
        assertNull(ret);
        assertTrue(player1.inGame());
        assertTrue(player2.inGame());
        assertEquals(CuT.getPlayersInGame().size(), 2);
        assertEquals(CuT.getAvailablePlayers().size(), 2);

        // Tests spectating a user that doesn't exist
        ret = CuT.spectateGame(player3, "Bob");
        assertEquals(ret.getType(), Message.Type.ERROR);
        assertEquals(CuT.getPlayersInGame().size(), 2);
        assertEquals(CuT.getAvailablePlayers().size(), 2);

        // Tests spectating a user that isn't in a game
        ret = CuT.spectateGame(player3, player4.getName());
        assertEquals(ret, SPECTATE_NOT_IN_GAME);
        assertEquals(CuT.getPlayersInGame().size(), 2);
        assertEquals(CuT.getAvailablePlayers().size(), 2);

        // Tests spectating a user that is in a game
        ret = CuT.spectateGame(player3, player1.getName());
        assertNull(ret);
        assertEquals(CuT.getPlayersInGame().size(), 2);
        assertEquals(CuT.getAvailablePlayers().size(), 1);
    }
}
