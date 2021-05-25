package com.webcheckers.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import spark.*;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;

/**
 * the {@code POST /signOut} route handler.
 *
 * @author <a href='spm8848@rit.edu'>Sean McDonnell </a>
 */
@Tag("UI-tier")
public class PostSignOutRouteTest {
    /**
     * The component-under-test (CuT)
     */
    private PostSignoutRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerLobby;
    private final String CURRENT_USER_ATTR = "currentUser";
    private final String OPPONENT_NAME = "OPPONENT";
    static final String RESIGN_GAME_MESSAGE = "The game has been resigned.";
    private Player currentUser;
    private Player opponent;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        currentUser = new Player(CURRENT_USER_ATTR);
        opponent = new Player(OPPONENT_NAME);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();
        CuT = new PostSignoutRoute(playerLobby);
    }

    
    @Test
    public void testNormalSignOut(){
        //Check current user
        when(session.attribute(CURRENT_USER_ATTR)).thenReturn(currentUser);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        //Start Test
        Object returnedValue = CuT.handle(request, response);
        Assertions.assertEquals(null, returnedValue);

        //Check that the player was removed from the 
        Assertions.assertEquals(null, playerLobby.findPlayer(CURRENT_USER_ATTR));
        //Check that the game is no longer active
        Assertions.assertFalse(currentUser.getGame().isActive());  
    }


    /**
     * Check signout functions as expected when the session is null
    */
    @Test
    public void testNullSession(){
        //Check current user
        when(request.session()).thenReturn(null);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        //Start Test
        Object returnedValue = CuT.handle(request, response);
        //Check proper return
        Assertions.assertEquals(null, returnedValue);

        //Check that the player was removed from the 
        Assertions.assertEquals(currentUser, playerLobby.findPlayer(CURRENT_USER_ATTR));

        //Check that the game is still active
        Assertions.assertTrue(currentUser.getGame().isActive());  
        
    }

    /**
     * Check signout functions as expected when the user is null
     */
    @Test
    public void testNullUser(){
        when(session.attribute(CURRENT_USER_ATTR)).thenReturn(null);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        //Start Test
        Object returnedValue = CuT.handle(request, response);
        //Check proper return
        Assertions.assertEquals(null, returnedValue);

        //Check that the player was not removed from the player lobby
        Assertions.assertEquals(currentUser, playerLobby.findPlayer(CURRENT_USER_ATTR));

        //Check that the game is still active
        Assertions.assertTrue(currentUser.getGame().isActive());  
    }
    
}
