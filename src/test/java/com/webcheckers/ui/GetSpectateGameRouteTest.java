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
 * Testing the functions of GetSpectateGameRoute
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */

@Tag("UI-tier")
public class GetSpectateGameRouteTest {

    private static final String VIEW_NAME = "game.ftl";
    private static final String CURRENT_USER = "currentUser";

    /**
     * The component-under-test (CuT)
     */
    private GetSpectateGameRoute CuT;

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
    private Player player3;
    private String player1Name;
    private String player2Name;
    private String player3Name;

    @BeforeEach
    public void setup() {
        player1Name = "player1";
        player2Name = "player2";
        player3Name = "player3";
        request = mock(Request.class);
        session = mock(Session.class);
        player1 = new Player(player1Name);
        player2 = new Player(player2Name);
        player3 = new Player(player3Name);
        when(request.session()).thenReturn(session);
        when(request.session().attribute(eq(CURRENT_USER))).thenReturn(player1);
        engine = mock(TemplateEngine.class);
        lobby = new PlayerLobby();
        lobby.signIn(player1);
        lobby.signIn(player2);
        lobby.signIn(player3);
        response = mock(Response.class);

        CuT = new GetSpectateGameRoute(engine, lobby);
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
    public void get_spectate_game_route_no_session(){
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
     * make sure the proper view is rendered when the user is in an active game and is a spectator
     */
    @Test
    public void get_game_route_in_active_game_red_player(){
        lobby.startGame(player1, player2.getName());
        lobby.spectateGame(player3, player1Name);

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
        testHelper.assertViewModelAttribute("viewMode", "SPECTATOR");
        testHelper.assertViewModelAttribute("redPlayer", player1);
        testHelper.assertViewModelAttribute("whitePlayer", player2);
        testHelper.assertViewModelAttribute("activeColor", "RED");
        //     * view name is correct
        testHelper.assertViewName(VIEW_NAME);
    }


    /**
     * make sure the proper view is rendered when a user is in an inactive game
     */
    @Test
    public void get_game_route_inactive_game(){
        lobby.startGame(player1, player2.getName());
        lobby.spectateGame(player3, player1Name);
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
