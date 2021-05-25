package com.webcheckers.ui;

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
 * Unit Testing
 *
 *  @author <a href='spm8848@rit.edu'>Sean McDonnell</a>
 *  @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 *  @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("UI-tier")
public class PostSigninRouteTest {

    private static final String CURRENT_USER_USERNAME = "CURRENT USER";
    private static final String INVALID_USERNAME_MESSAGE = "This username is not valid.";

    /**
     * The component-under-test (CuT)
     */
    private PostSigninRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private PlayerLobby playerLobby;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);
        playerLobby = new PlayerLobby();

        CuT = new PostSigninRoute(engine, playerLobby);
    }

    /**
     * Testing the conditional logic when returnMessage == null
     */

    @Test
    public void test_null_message_conditional() {
        when(request.queryParams(PostSigninRoute.CURRENT_USER_ATTR)).thenReturn(CURRENT_USER_USERNAME);

        CuT.handle(request, response);

        Assertions.assertEquals(null, session.attribute(PostSigninRoute.CURRENT_USER_ATTR));

        verify(response).redirect(WebServer.HOME_URL);
    }

    /**
     * Testing the conditional logic when returnMessage != null
     */

    @Test
    public void test_non_null_message_conditional() {
        final TemplateEngineTester testHelper = new TemplateEngineTester();

        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());
        when(request.queryParams(PostSigninRoute.CURRENT_USER_ATTR)).thenReturn(null);

        CuT.handle(request, response);

        Message expectedReturnMessage = Message.error(INVALID_USERNAME_MESSAGE);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(PostSigninRoute.RETURN_MESSAGE_ATTR, expectedReturnMessage);
        //   * test view name
        testHelper.assertViewName(PostSigninRoute.SIGN_IN_VIEW_NAME);
    }

}
