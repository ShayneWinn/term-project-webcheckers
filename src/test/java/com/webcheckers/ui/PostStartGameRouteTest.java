package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import spark.*;

/**
 * Testing the functionality of the PostStartGameRoute
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 */
@Tag("UI-tier")
public class PostStartGameRouteTest {

    private static final String CURRENT_USER = "currentUser";

    /**
     * The component-under-test (CuT)
     */
    private PostStartGameRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private Response response;

    // friendly
    private PlayerLobby playerLobby;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();

        CuT = new PostStartGameRoute(playerLobby);
    }

    /**
     * Verify that both the current user and opponent are valid targets to start a game
     */
    @Test
    public void testValidOpponent() {
        // create and sign in two valid users
        Player testUser1 = new Player("testUser1");
        Player testRequester1 = new Player("testRequester1");
        playerLobby.signIn(testUser1);
        playerLobby.signIn(testRequester1);

        // attach them as the params and attributes
        when(session.attribute(eq(CURRENT_USER))).thenReturn(testRequester1);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser1");

        // call the CuT
        CuT.handle(request, response);

        // make sure we are redirected to the game
        verify(response).redirect(eq("/game"));

        // not sure if these should be here?
        Assertions.assertFalse(playerLobby.isAvailable(testUser1.getName()));
        Assertions.assertFalse(playerLobby.isAvailable(testRequester1.getName()));
    }

    /**
     * Verify that when an opponent is not real then the route will redirect you home with an error message
     */
    @Test
    public void testNullOpponent(){
        // create only one player
        Player testRequester1 = new Player("testRequester1");
        playerLobby.signIn(testRequester1);

        // add "testUser1" as the requested(this user does not exist)
        when(session.attribute(eq(CURRENT_USER))).thenReturn(testRequester1);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser1");

        // call the CuT
        CuT.handle(request, response);

        // verify that we are redirected with an error
        verify(response).redirect(Mockito.argThat(s -> s.contains("/?redirectMessage=") && s.contains("&t=")));
    }

    /**
     * Verify that if an opponent is in game that you will be redirected home with and error
     */
    @Test
    public void testOpponentInGame(){
        // Create game between two users
        Player testUser1 = new Player("testUser1");
        Player testUser2 = new Player("testUser2");
        playerLobby.signIn(testUser1);
        playerLobby.signIn(testUser2);

        when(session.attribute(eq(CURRENT_USER))).thenReturn(testUser1);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser2");
        CuT.handle(request, response);

        verify(response).redirect(eq("/game"));

        // create actual requester
        Player testRequester1 = new Player("testRequester1");
        playerLobby.signIn(testRequester1);

        // set them as the currentUser
        when(session.attribute(eq(CURRENT_USER))).thenReturn(testRequester1);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser1");

        // call the CuT
        CuT.handle(request, response);

        // verify that we are redirected home with an error
        verify(response).redirect(Mockito.argThat(s -> s.contains("/?redirectMessage=") && s.contains("&t=")));
    }

    /**
     * Verify that when the currentUser is not signed in that the server will redirect you home
     */
    @Test
    public void testInvalidCurrentUser(){
        // create two Players but only sign one in
        Player testUser1 = new Player("testUser1");
        Player testRequester1 = new Player("testRequester1");
        playerLobby.signIn(testUser1);

        // set them at attributes
        when(session.attribute(eq(CURRENT_USER))).thenReturn(testRequester1);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser1");

        // call
        CuT.handle(request, response);

        // make sure we are sent home with no error
        verify(response).redirect(eq("/"));
    }

    /**
     * Verify that if the currentUser is not set that the server will redirect you home
     */
    @Test
    public void testNullCurrentUser(){
        // create only one valid user
        Player testUser1 = new Player("testUser1");
        playerLobby.signIn(testUser1);

        // set the currentUser to be null
        when(session.attribute(eq(CURRENT_USER))).thenReturn(null);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser1");

        // call the CuT
        CuT.handle(request, response);

        // make sure we are redirected home
        verify(response).redirect(eq("/"));
    }

    /**
     * Verify that if the session is null that the server will redirect you home
     */
    @Test
    public void testInvalidSession(){
        // create only one valid user
        Player testUser1 = new Player("testUser1");
        playerLobby.signIn(testUser1);

        // set the session to null
        when(request.session()).thenReturn(null);
        when(request.queryParams(eq("otherUser"))).thenReturn("testUser1");

        // call the CuT
        CuT.handle(request, response);

        // make sure we are redirected home
        verify(response).redirect(eq("/"));
    }
}
