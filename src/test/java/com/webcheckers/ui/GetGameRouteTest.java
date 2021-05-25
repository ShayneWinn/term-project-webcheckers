package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import spark.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

/**
 * Testing the functions of GetGameRoute
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */

@Tag("UI-tier")
public class GetGameRouteTest {

    private static final String VIEW_NAME = "game.ftl";
    private static final String CURRENT_USER = "currentUser";

    /**
     * The component-under-test (CuT)
     */
    private GetGameRoute CuT;

    /**
     * Attributes holding mock objects
     */
    private Request request;
    private Session session;
    private TemplateEngine engine;
    private Response response;
    private PlayerLobby lobby;
    private Player player1;
    private Player player2;
    private String player1Name;
    private String player2Name;

    @BeforeEach
    public void setup() {
        player1Name = "player1";
        player2Name = "player2";
        request = mock(Request.class);
        session = mock(Session.class);
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        when(request.session()).thenReturn(session);
        when(request.session().attribute(eq(CURRENT_USER))).thenReturn(player1);
        engine = mock(TemplateEngine.class);
        lobby = new PlayerLobby();
        lobby.signIn(player1);
        lobby.signIn(player2);
        response = mock(Response.class);

        CuT = new GetGameRoute(engine, lobby);
    }

    /**
     * Tests the creation of View-Model
     */
    @Test
    public void testViewModel() {
        lobby.startGame(player1, player2.getName());

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
     * make sure that a user is redirected home when there is no session
     */
    @Test
    public void get_game_route_no_session(){
        when(request.session()).thenReturn(null);

        CuT.handle(request, response);

        verify(response).redirect(eq(GetHomeRoute.HOME_ROUTE));
    }

    /**
     * check that a user is redirected home when there is no currentUser
     */
    @Test
    public void get_game_route_no_current_user(){
        when(request.session().attribute(eq(CURRENT_USER))).thenReturn(null);

        CuT.handle(request, response);

        verify(response).redirect(eq(GetHomeRoute.HOME_ROUTE));
    }

    /**
     * check that the user is redirected home when there is no game
     */
    @Test
    public void get_game_route_no_game(){
        CuT.handle(request, response);

        verify(response).redirect(eq(GetHomeRoute.HOME_ROUTE));
    }

    /**
     * check that the user is redirected home when they are in a non-real game
     * (should never happen, here just in case)
     */
    @Test
    public void get_game_route_in_non_real_game(){
        Game mockGame = mock(Game.class);
        player1.joinGame(mockGame);

        CuT.handle(request, response);

        verify(response).redirect(eq(GetHomeRoute.HOME_ROUTE));
    }

    /**
     * make sure the proper view is rendered when the user is in an active game and is red player
     */
    @Test
    public void get_game_route_in_active_game_red_player(){
        lobby.startGame(player1, player2.getName());

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // start test
        CuT.handle(request, response);

        // check results
        //     * model is non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //     * model contains all necessary values
        testHelper.assertViewModelAttributeIsAbsent("modeOptionsAsJSON");
        testHelper.assertViewModelAttribute("gameID", player1.getGame().getGameID());
        testHelper.assertViewModelAttribute("currentUser", player1);
        testHelper.assertViewModelAttribute("viewMode", "PLAY");
        testHelper.assertViewModelAttribute("redPlayer", player1);
        testHelper.assertViewModelAttribute("whitePlayer", player2);
        testHelper.assertViewModelAttribute("activeColor", "RED");

        //     * view name is correct
        testHelper.assertViewName(VIEW_NAME);
    }

    /**
     * make sure the proper view is rendered when the user is in an active game
     */
    @Test
    public void get_game_route_in_active_game_white_player(){
        lobby.startGame(player2, player1.getName());

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // start test
        CuT.handle(request, response);

        // check results
        //     * model is non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //     * model contains all necessary values
        testHelper.assertViewModelAttributeIsAbsent("modeOptionsAsJSON");
        testHelper.assertViewModelAttribute("gameID", player1.getGame().getGameID());
        testHelper.assertViewModelAttribute("currentUser", player1);
        testHelper.assertViewModelAttribute("viewMode", "PLAY");
        testHelper.assertViewModelAttribute("redPlayer", player2);
        testHelper.assertViewModelAttribute("whitePlayer", player1);
        testHelper.assertViewModelAttribute("activeColor", "RED");
        //          this needs to be fixed
        //testHelper.assertViewModelAttribute("board", player1.getGame().getBoardView(player1));
        //     * view name is correct
        testHelper.assertViewName(VIEW_NAME);
    }

    /**
     * make sure the proper view is rendered when a user is in an inactive game
     */
    @Test
    public void get_game_route_inactive_game(){
        lobby.startGame(player1, player2.getName());
        lobby.signOut(player2);
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        CuT.handle(request, response);

        Gson gson = new Gson();
        final Map<String, Object> modeOptions = new HashMap<>(2);
        modeOptions.put("isGameOver", true);
        modeOptions.put("gameOverMessage", player1.getGame().getGameEndedReason().getText());
        Object expected = gson.toJson(modeOptions);

        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewModelAttribute("modeOptionsAsJSON", expected);
        testHelper.assertViewName(VIEW_NAME);
    }

}
