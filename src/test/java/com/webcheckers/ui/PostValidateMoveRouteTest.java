package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.model.Game;
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
import com.webcheckers.model.Player;


/**
 * Unit testing of PostValidateMoveRoute functionality
 *
 *  @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("UI-tier")
public class PostValidateMoveRouteTest {

    private static final String CURRENT_USER_USERNAME = "CURRENT USER";
    private static final String OPPONENT_USERNAME = "OPPONENT";
    private static final String STRING_CONTAINS_NULL = "null";
    private static final String EMPTY_STRING = "";
    private static final String SIMPLE_MOVE_JSON = "{\"start\":{\"row\":2,\"cell\":7},\"end\":{\"row\":3,\"cell\":6}}";
    private static final String VALID_SIMPLE_MOVE_MESSAGE = "You moved a piece.";
    /**
     * The component-under-test (CuT)
     */
    private PostValidateMoveRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private Response response;
    private Gson gson;
    private PlayerLobby playerLobby;
    private Player currentUser;
    private Player opponent;
    private Message expectedReturnMessage;
    private Object messageRouteReturns;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        gson = new Gson();
        playerLobby = new PlayerLobby();

        CuT = new PostValidateMoveRoute(gson, playerLobby);
    }

    /**
     * Test proper functionality when moveAsJson is "null"
     */
    @Test
    public void test_null_moveAsJson() {
        when(request.queryParams(PostValidateMoveRoute.ACTION_DATA_QUERY_PARAM)).thenReturn(STRING_CONTAINS_NULL);

        currentUser = new Player(CURRENT_USER_USERNAME);
        opponent = new Player(OPPONENT_USERNAME);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        when(session.attribute(PostValidateMoveRoute.CURRENT_USER_ATTR)).thenReturn(currentUser);

        expectedReturnMessage = Message.error(PostValidateMoveRoute.OCCUPIED_SPACE);

        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper functionality when moveAsJson is empty
     */
    @Test
    public void test_empty_moveAsJson() {
        when(request.queryParams(PostValidateMoveRoute.ACTION_DATA_QUERY_PARAM)).thenReturn(EMPTY_STRING);

        currentUser = new Player(CURRENT_USER_USERNAME);
        opponent = new Player(OPPONENT_USERNAME);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        when(session.attribute(PostValidateMoveRoute.CURRENT_USER_ATTR)).thenReturn(currentUser);

        expectedReturnMessage = Message.error(PostValidateMoveRoute.NO_POSITION_PROVIDED_MESSAGE);

        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper functionality when moveAsJson is a valid simple move JSON String, and
     * there are no pending move boards before move validation
     */
    @Test
    public void test_valid_simple_move_moveAsJson_and_no_pending_move_boards() {
        when(request.queryParams(PostValidateMoveRoute.ACTION_DATA_QUERY_PARAM)).thenReturn(SIMPLE_MOVE_JSON);

        currentUser = new Player(CURRENT_USER_USERNAME);
        opponent = new Player(OPPONENT_USERNAME);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        when(session.attribute(PostValidateMoveRoute.CURRENT_USER_ATTR)).thenReturn(currentUser);

        expectedReturnMessage = Message.info(VALID_SIMPLE_MOVE_MESSAGE);

        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

    /**
     * Test proper functionality when moveAsJson is a valid simple move JSON String, and
     * there is a pending move board before move validation
     */
    @Test
    public void test_valid_simple_move_moveAsJson_and_pending_move_board() {
        when(request.queryParams(PostValidateMoveRoute.ACTION_DATA_QUERY_PARAM)).thenReturn(SIMPLE_MOVE_JSON);

        currentUser = new Player(CURRENT_USER_USERNAME);
        opponent = new Player(OPPONENT_USERNAME);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        when(session.attribute(PostValidateMoveRoute.CURRENT_USER_ATTR)).thenReturn(currentUser);

        Game currentUserGame = currentUser.getGame();

        currentUserGame.addToPendingMoveBoards(currentUserGame.createBoardCopy(currentUserGame.getBoard()));

        expectedReturnMessage = Message.info(VALID_SIMPLE_MOVE_MESSAGE);

        messageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(expectedReturnMessage.toJson(), messageRouteReturns);
    }

}

