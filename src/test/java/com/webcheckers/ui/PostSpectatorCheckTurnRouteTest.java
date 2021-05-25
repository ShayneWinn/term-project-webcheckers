package com.webcheckers.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

import spark.*;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;


/**
 * The unit test suite for the {@link PostSpectatorCheckTurnRoute} component.
 *
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 */
@Tag("UI-tier")
public class PostSpectatorCheckTurnRouteTest {

    /** the Component-under-test (CuT)*/
    private PostSpectatorCheckTurnRoute CuT;

    /** Attributes holding mock objects */
    private Request request;
    private Session session;
    private Response response;
    private Player currentUser;

    /** Constants */
    static final String CURRENT_USER_ATTR = "currentUser";
    static final String CURRENT_USER_NAME = "CURRENT USER";
    static final String TRUE_MESSAGE = Message.info(PostSpectatorCheckTurnRoute.TRUE).toJson();
    static final String FALSE_MESSAGE = Message.info(PostSpectatorCheckTurnRoute.FALSE).toJson();

    /**
     * Initialize CuT and mock classes before each test
     */
    @BeforeEach
    private void setup(){
        // CuT initialize
        CuT = new PostSpectatorCheckTurnRoute();

        // players
        currentUser = mock(Player.class);
        when(currentUser.getName()).thenReturn(CURRENT_USER_NAME);

        // web server classes
        session = mock(Session.class);
        when(session.attribute(CURRENT_USER_ATTR)).thenReturn(currentUser);

        request = mock(Request.class);
        when(request.session()).thenReturn(session);

        response = mock(Response.class);
    }

    /**
     * Verify correct JSON Message returned when a change in turn has occurred
     */
    @Test
    public void check_return_message_when_change_occurred(){
        when(currentUser.spectateDidTurnUpdate()).thenReturn(true);

        // call the CuT
        Object retObject = CuT.handle(request, response);

        // analyze results
        //      - check change turn called
        verify(currentUser).spectateDidTurnUpdate();
        //      - returned a string (JSON string)
        Assertions.assertEquals(retObject.getClass(), String.class);
        //      - returned the proper message
        Assertions.assertEquals(retObject, TRUE_MESSAGE);
    }

    /**
     * Verify correct JSON Message returned when NO change in turn has occurred
     */
    @Test
    public void check_return_message_when_no_change_occurred(){
        when(currentUser.spectateDidTurnUpdate()).thenReturn(false);

        // call the CuT
        Object retObject = CuT.handle(request, response);

        // analyze results
        //      - check change turn called
        verify(currentUser).spectateDidTurnUpdate();
        //      - returned a string (JSON string)
        Assertions.assertEquals(retObject.getClass(), String.class);
        //      - returned the proper message
        Assertions.assertEquals(retObject, FALSE_MESSAGE);
    }
}
