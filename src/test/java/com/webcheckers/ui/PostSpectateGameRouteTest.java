package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import org.mockito.Mockito;
import spark.*;

/**
 * Testing the functionality of the PostSpectateGameRoute
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 */
@Tag("UI-tier")
public class PostSpectateGameRouteTest {

    private static final String CURRENT_USER = "currentUser";

    /**
     * The component-under-test (CuT)
     */
    private PostSpectateGameRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private Response response;
    private Player player1;
    private Player player2;
    private Player player3;
    private String player1Name;
    private String player2Name;
    private String player3Name;

    // friendly
    private PlayerLobby playerLobby;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();
        player1Name = "player1";
        player2Name = "player2";
        player3Name = "player3";
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        player3 = new Player(player3Name);
        playerLobby.signIn(player1);
        playerLobby.signIn(player2);
        playerLobby.signIn(player3);

        CuT = new PostSpectateGameRoute(playerLobby);
    }

    /**
     * Verify that both the current user and users in game are valid targets to spectate a game
     */
    @Test
    public void testValidSpectate() {

        // attach them as the params and attributes
        when(session.attribute(eq(CURRENT_USER))).thenReturn(player3);
        when(request.queryParams(eq("otherUser"))).thenReturn(player1Name);

        playerLobby.startGame(player1, player1Name);
        // call the CuT
        CuT.handle(request, response);

        // make sure we are redirected to the game
        verify(response).redirect(eq("/spectator/game"));

    }

    /**
     * Verify that when an user is not real then the route will redirect you home with an error message
     */
    @Test
    public void testNullSpectate(){

        // add "testUser1" as the requested(this user does not exist)
        when(session.attribute(eq(CURRENT_USER))).thenReturn(player3);
        when(request.queryParams(eq("otherUser"))).thenReturn("invalid user");

        playerLobby.spectateGame(player3, "invalid user");
        // call the CuT
        CuT.handle(request, response);

        // verify that we are redirected with an error
        verify(response).redirect(Mockito.argThat(s -> s.contains("/?redirectMessage=") && s.contains("&t=")));
    }

    /**
     * Verify that when the currentUser is not signed in that the server will redirect you home
     */
    @Test
    public void testInvalidCurrentUser(){

        //Signing out player2
        playerLobby.signOut(player2);

        // set them at attributes
        when(session.attribute(eq(CURRENT_USER))).thenReturn(player2);
        when(request.queryParams(eq("otherUser"))).thenReturn(player1Name);

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

        // set the currentUser to be null
        when(session.attribute(eq(CURRENT_USER))).thenReturn(null);
        when(request.queryParams(eq("otherUser"))).thenReturn(player1Name);

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

        // set the session to null
        when(request.session()).thenReturn(null);
        when(request.queryParams(eq("otherUser"))).thenReturn(player1Name);

        // call the CuT
        CuT.handle(request, response);

        // make sure we are redirected home
        verify(response).redirect(eq("/"));
    }
}
