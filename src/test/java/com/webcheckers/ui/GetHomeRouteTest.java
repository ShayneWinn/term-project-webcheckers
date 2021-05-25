package com.webcheckers.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;
import com.webcheckers.ui.TemplateEngineTester;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.Session;
import spark.TemplateEngine;

import java.util.ArrayList;

/**
 * The unit test suite for the {@link GetHomeRoute} component.
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 */
@Tag("UI-tier")
public class GetHomeRouteTest {

    /**
     * The component-under-test (CuT)
     */
    private GetHomeRoute CuT;

    // friendly objects
    private PlayerLobby playerLobby;
    private ArrayList<Player> availablePlayers;
    private Message infoMsg;
    private Message errorMsg;
    private final String INFO_STRING = "INFO";
    private final String CURRENT_USER_NAME = "CURRENT USER";
    private final String OPPONENT_NAME = "OPPONENT";
    private Player currentUser;
    private Player opponent;

    // attributes holding mock objects
    private TemplateEngine engine;
    private Request request;
    private Session session;
    private Response response;

    /**
     * Setup new mock objects for each test.
     */
    @BeforeEach
    public void setup() {
        playerLobby = new PlayerLobby();
        availablePlayers = new ArrayList<>(playerLobby.getAvailablePlayers());
        currentUser = new Player(CURRENT_USER_NAME);
        opponent = new Player(OPPONENT_NAME);
        engine = mock(TemplateEngine.class);
        request = mock(Request.class);
        session = mock(Session.class);
        when(request.session()).thenReturn(session);
        when(request.session().attribute(GetHomeRoute.CURRENT_USER_ATTR)).thenReturn(currentUser);
        response = mock(Response.class);

        // create a unique CuT for each test
        // the GameCenter is friendly but the engine mock will need configuration
        CuT = new GetHomeRoute(engine, playerLobby);
    }

    /**
     * Test that CuT shows the correct Home view when the current user isn't in a game and
     * there isn't a redirect message in the http session.
     */
    @Test
    public void session_without_current_user_in_game_with_no_redirect_message() {

        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        availablePlayers = new ArrayList<>(playerLobby.getAvailablePlayers());

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSG_ATTR, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.AVAILABLE_PLAYERS_ATTR, availablePlayers);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ONLINE_ATTR, playerLobby.getPlayerCount());
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        //   * verify that a redirect message is not stored in the http session
        assertNull(request.queryParams(GetHomeRoute.REDIRECT_MESSAGE_PARAM));
    }

    /**
     * Test that CuT shows the correct Home view when the current user isn't in a game and
     * there is a redirect message in the http session that is of type Info.
     */
    @Test
    public void session_without_current_user_in_game_with_an_info_redirect_message() {

        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        availablePlayers = new ArrayList<>(playerLobby.getAvailablePlayers());

        when(request.queryParams(GetHomeRoute.REDIRECT_MESSAGE_PARAM)).thenReturn(INFO_STRING);
        when(request.queryParams(GetHomeRoute.REDIRECT_MESSAGE_TYPE_PARAM)).thenReturn(INFO_STRING);

        infoMsg = Message.info(INFO_STRING);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSG_ATTR, infoMsg);
        testHelper.assertViewModelAttribute(GetHomeRoute.AVAILABLE_PLAYERS_ATTR, availablePlayers);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ONLINE_ATTR, playerLobby.getPlayerCount());
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * Test that CuT shows the correct Home view when the current user isn't in a game and
     * there is a redirect message in the http session that is of type Error.
     */
    @Test
    public void session_without_current_user_in_game_with_an_error_redirect_message() {

        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        availablePlayers = new ArrayList<>(playerLobby.getAvailablePlayers());

        when(request.queryParams(GetHomeRoute.REDIRECT_MESSAGE_PARAM)).thenReturn(GetHomeRoute.ERROR_STRING);
        when(request.queryParams(GetHomeRoute.REDIRECT_MESSAGE_TYPE_PARAM)).thenReturn(GetHomeRoute.ERROR_STRING);

        errorMsg = Message.error(GetHomeRoute.ERROR_STRING);

        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSG_ATTR, errorMsg);
        testHelper.assertViewModelAttribute(GetHomeRoute.AVAILABLE_PLAYERS_ATTR, availablePlayers);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ONLINE_ATTR, playerLobby.getPlayerCount());
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * Test that CuT shows a Game view when the current user is in a game and not a spectator.
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    @Test
    public void session_with_non_spectator_current_user_in_game() {

        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);

        availablePlayers = new ArrayList<>(playerLobby.getAvailablePlayers());

        // start a game between current user and an opponent
        playerLobby.startGame(currentUser, opponent.getName());

        // invoke test
        CuT.handle(request, response);

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.GAME_URL);
    }

    /**
     * Test that CuT shows a spectator game view when the current user is in a game and a spectator.
     */
    @Test
    public void session_with_spectator_current_user_in_game() {

        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);

        // start a game between current user and an opponent
        playerLobby.startGame(currentUser, opponent.getName());

        // spectate the game
        currentUser.spectateGame(opponent.getGame());

        // invoke test
        CuT.handle(request, response);

        // Analyze the results:
        //   * redirect to the Game view
        verify(response).redirect(WebServer.SPECTATE_GAME);
    }

    /**
     * make sure that when a user connects with an inactive game they are removed from
     *     the game and kept on the home page
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    @Test
    public void session_with_user_in_inactive_game(){
        //setup
        playerLobby.signIn(currentUser);
        playerLobby.signIn(opponent);
        playerLobby.startGame(currentUser, opponent.getName());

        // template engine test helper
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // end game
        playerLobby.signOut(opponent);
        assertFalse(currentUser.isGameActive());
        assertNotNull(currentUser.getGame());

        // test
        CuT.handle(request, response);

        // make sure the user left the game
        assertNull(currentUser.getGame());

        // make sure page rendered
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * make sure that when a user is in a non-real game they are kept on home and told to leave the game
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    @Test
    public void session_with_user_in_unreal_game(){
        //setup
        playerLobby.signIn(currentUser);
        Game mockGame = mock(Game.class);

        // template engine test helper
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // make user join game
        currentUser.joinGame(mockGame);

        // test
        CuT.handle(request, response);

        // make sure user left
        assertNull(currentUser.getGame());

        // make sure page rendered
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
    }

    /**
     * make sure a null currentUser is still shown the homepage
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    @Test
    public void session_with_null_current_user(){
        // set currentUser to null
        when(session.attribute(GetHomeRoute.CURRENT_USER_ATTR)).thenReturn(null);

        // template engine test helper
        final TemplateEngineTester testHelper = new TemplateEngineTester();
        when(engine.render(any(ModelAndView.class))).thenAnswer(testHelper.makeAnswer());

        // Invoke the test
        CuT.handle(request, response);

        // Analyze the results:
        //   * model is a non-null Map
        testHelper.assertViewModelExists();
        testHelper.assertViewModelIsaMap();
        //   * model contains all necessary View-Model data
        testHelper.assertViewModelAttribute(GetHomeRoute.TITLE_ATTR, GetHomeRoute.TITLE);
        testHelper.assertViewModelAttribute(GetHomeRoute.MSG_ATTR, GetHomeRoute.WELCOME_MSG);
        testHelper.assertViewModelAttribute(GetHomeRoute.AVAILABLE_PLAYERS_ATTR, availablePlayers);
        testHelper.assertViewModelAttribute(GetHomeRoute.PLAYERS_ONLINE_ATTR, playerLobby.getPlayerCount());
        //   * test view name
        testHelper.assertViewName(GetHomeRoute.VIEW_NAME);
        //   * verify that a redirect message is not stored in the http session
        assertNull(request.queryParams(GetHomeRoute.REDIRECT_MESSAGE_PARAM));
    }

}
