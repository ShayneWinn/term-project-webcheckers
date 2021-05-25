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
import com.webcheckers.util.Message;


/**
 * The unit test suite for the {@link PostResignGameRoute} component.
 *
 * @author <a href='mailto:spm8848@rit.edu'>Sean McDonnell</a>
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("UI-tier")
public class PostResignGameRouteTest {

    /**
     * The component-under-test (CuT)
     */
    private PostResignGameRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private Response response;
    private PlayerLobby playerLobby;
    private final String CURRENT_USER_ATTR = "currentUser";
    private final String CURRENT_USER_NAME = "CURRENT USER";
    private final String CURRENT_USER_RESIGN_MESSAGE = CURRENT_USER_NAME + " has resigned the match";
    private final String OPPONENT_NAME = "OPPONENT";
    static final String RESIGN_GAME_MESSAGE = "The game has been resigned.";
    private Player currentUser;
    private Player opponent;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        currentUser = new Player(CURRENT_USER_NAME);
        opponent = new Player(OPPONENT_NAME);
        when(request.session()).thenReturn(session);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();
        CuT = new PostResignGameRoute(playerLobby);
    }

    /**
     * Verify that player resign returns correctly, and verify that the correct JSON returns
     */
    @Test
    public void test_JSON_return_value_with_non_null_session_and_non_null_current_user(){
        //Set expected return message
        Message returnMessage = Message.info(RESIGN_GAME_MESSAGE);

        when(session.attribute(CURRENT_USER_ATTR)).thenReturn(currentUser);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        //Invoke Test and capture return value when player is not null
        Object returnMessageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(CURRENT_USER_RESIGN_MESSAGE,
                playerLobby.findPlayer(CURRENT_USER_NAME).getGame().getGameEndedReason().getText());

        //Verify JSON return value
        Assertions.assertEquals(returnMessageRouteReturns, returnMessage.toJson());
    }

    /**
     * Verify that the correct json returns with a null session
    */
    @Test
    public void test_JSON_return_value_with_null_session(){
        //Set expected return message
        Message returnMessage = Message.info(RESIGN_GAME_MESSAGE);

        when(request.session()).thenReturn(null);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        //Invoke Test and capture return value when player is not null
        Object returnMessageRouteReturns = CuT.handle(request, response);

        //Verify JSON return value
        Assertions.assertEquals(returnMessageRouteReturns, returnMessage.toJson());
    }

    /**
     * Verify that the correct json returns with a null current user session attribute value
     */
    @Test
    public void test_JSON_return_value_with_null_current_user(){
        //Set expected return message
        Message returnMessage = Message.info(RESIGN_GAME_MESSAGE);

        when(session.attribute(CURRENT_USER_ATTR)).thenReturn(null);

        //Create a game
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        //Invoke Test and capture return value when player is not null
        Object returnMessageRouteReturns = CuT.handle(request, response);

        Assertions.assertEquals(returnMessage.toJson(), returnMessageRouteReturns);
    }

}
