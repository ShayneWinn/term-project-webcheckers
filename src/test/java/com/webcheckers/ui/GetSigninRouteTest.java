package com.webcheckers.ui;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import spark.*;

/**
 * Testing the functionality of the GetSignin Route
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */

@Tag("UI-tier")
public class GetSigninRouteTest {

    private static final String CURRENT_USER = "currentUser";
    private static final String VIEW_NAME = "signin.ftl";

    /**
     * The component-under-test (CuT)
     */
    private GetSigninRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;

    @BeforeEach
    public void setup() {
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        engine = mock(TemplateEngine.class);
        response = mock(Response.class);

        CuT = new GetSigninRoute(engine);
    }

    /**
     * Tests the creation of the View-Model
     */

    @Test
    public void testViewModel() {

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        //Testing proper creation of the View-Model
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();

        //Testing proper ftl is being sent
        testHelper.assertViewName(VIEW_NAME);

    }

    /**
     * Testing the proper rendering of the template engine
     */
    @Test
    public void testTemplateEngine() {

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        //Testing proper ftl is being sent
        testHelper.assertViewName(VIEW_NAME);

    }

    /**
     * Tests the conditional logic when currentUser != null
     */
    @Test
    public void testCurrentUser() {

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        //Forces conditional logic to be true
        when(session.attribute("currentUser")).thenReturn(CURRENT_USER);

        //Testing the return value to be null
        Object result = CuT.handle(request, response);
        Assertions.assertNull(result);

    }

}
