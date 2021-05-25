package com.webcheckers.ui;

import com.webcheckers.model.Game;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import spark.*;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;


/**
 * The unit test suite for the {@link PostCheckTurnRoute} component.
 *
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 */
@Tag("UI-tier")
public class PostCheckTurnRouteTest {

    /** the Component-under-test (CuT)*/
    private PostCheckTurnRoute CuT;

    /** Attributes holding mock objects */
    private Request request;
    private Session session;
    private Response response;
    private Player currentUser;
    private Player opponent;

    /** Constants */
    static final String CURRENT_USER_ATTR = "currentUser";
    static final String CURRENT_USER_NAME = "CURRENT USER";
    static final String OPPONENT_NAME = "OPPONENT";
    static final String TRUE_MESSAGE = Message.info(PostCheckTurnRoute.TRUE).toJson();
    static final String FALSE_MESSAGE = Message.info(PostCheckTurnRoute.FALSE).toJson();

    /**
     * Initialize CuT and mock classes before each test
     */
    @BeforeEach
    private void setup(){
        // CuT initialize
        CuT = new PostCheckTurnRoute();

        // players
        currentUser = mock(Player.class);
        when(currentUser.getName()).thenReturn(CURRENT_USER_NAME);
        opponent = mock(Player.class);
        when(currentUser.getName()).thenReturn(OPPONENT_NAME);

        // web server classes
        session = mock(Session.class);
        when(session.attribute(CURRENT_USER_ATTR)).thenReturn(currentUser);

        request = mock(Request.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);
    }

    /**
     * Verify the correct message is being returned when a red player checks if
     *   it is their turn when it is their turn
     */
    @Test
    public void check_return_message_when_red_player_request_on_turn(){
        Game game = mock(Game.class); // create game
        when(game.isRedPlayerTurn()).thenReturn(true); // red players turn
        when(game.getPlayers()).thenReturn(new Player[]{currentUser, opponent}); // add players
        when(currentUser.getGame()).thenReturn(game);

        // call the CuT
        Object retObject = CuT.handle(request, response);

        // analyze results
        //      - returned a string (JSON string)
        Assertions.assertEquals(retObject.getClass(), String.class);
        //      - returned the proper message
        Assertions.assertEquals((String)retObject, TRUE_MESSAGE);

    }

    /**
     * Verify the correct message is being returned when a white player checks if
     *   it is their turn when it is their turn
     */
    @Test
    public void check_return_message_when_white_player_request_on_turn(){
        Game game = mock(Game.class); // create game
        when(game.isWhitePlayerTurn()).thenReturn(true); // white players turn
        when(game.getPlayers()).thenReturn(new Player[]{opponent, currentUser}); // add players
        when(currentUser.getGame()).thenReturn(game);

        // call the CuT
        Object retObject = CuT.handle(request, response);

        // analyze results
        //      - returned a string (JSON string)
        Assertions.assertEquals(retObject.getClass(), String.class);
        //      - returned the proper message
        Assertions.assertEquals((String)retObject, TRUE_MESSAGE);
    }

    /**
     * Verify the correct message is being returned when a red player checks if
     *   it is their turn when it is NOT their turn
     */
    @Test
    public void check_return_message_when_red_player_request_not_on_turn(){
        Game game = mock(Game.class); // create game
        when(game.isWhitePlayerTurn()).thenReturn(true); // white players turn
        when(game.getPlayers()).thenReturn(new Player[]{currentUser, opponent}); // add players
        when(currentUser.getGame()).thenReturn(game);

        // call the CuT
        Object retObject = CuT.handle(request, response);

        // analyze results
        //      - returned a string (JSON string)
        Assertions.assertEquals(retObject.getClass(), String.class);
        //      - returned the proper message
        Assertions.assertEquals((String)retObject, FALSE_MESSAGE);

    }

    /**
     * Verify the correct message is being returned when a white player checks if
     *   it is their turn when it is NOT their turn
     */
    @Test
    public void check_return_message_when_white_player_request_not_on_turn(){
        Game game = mock(Game.class); // create game
        when(game.isRedPlayerTurn()).thenReturn(true); // red players turn
        when(game.getPlayers()).thenReturn(new Player[]{opponent, currentUser}); // add players
        when(currentUser.getGame()).thenReturn(game);

        // call the CuT
        Object retObject = CuT.handle(request, response);

        // analyze results
        //      - returned a string (JSON string)
        Assertions.assertEquals(retObject.getClass(), String.class);
        //      - returned the proper message
        Assertions.assertEquals((String)retObject, FALSE_MESSAGE);
    }
}
