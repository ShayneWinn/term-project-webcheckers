package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;
import spark.Session;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the Game View page.
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 */
public class GetGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(GetGameRoute.class.getName());
    public static final String GAME_ROUTE = "/game";

    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /game} HTTP requests.
     *
     * @param templateEngine
     *   the HTML template rendering engine
     * @param playerLobby
     *   the {@link PlayerLobby} to handle player actions
     */
    public GetGameRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required");
        LOG.config("GetGameRoute is initialized.");
    }

    /**
     * Render the WebCheckers Game View page.
     *
     * @param request
     *   the HTTP request
     * @param response
     *   the HTTP response
     *
     * @return
     *   the rendered HTML for the Game View page
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetGameRoute is invoked.");
        Session httpSession = request.session();

        // Check if the current session and current user exist
        if(httpSession == null || httpSession.attribute("currentUser") == null) {
            // If the user or session do not exist, redirect to the home page
            response.redirect("/");
            return null;
        }

        // Get the current user's game 
        Player currentUser = httpSession.attribute("currentUser");

        // Check if the player is not in a game
        if(!currentUser.inGame()){
            // If the player is not in a game, redirect to home page
            response.redirect("/");
            return null;
        }

        // Check if game that the player is in is a real game
        if(!playerLobby.inRealGame(currentUser)){
            // If the game is not real, redirect the user to the home page
            response.redirect("/");
            return null;
        }

        // Holds non-HTTP Session view model
        Map<String, Object> vm = new HashMap<>();
        vm.put("title", "Game View");

        // Game Data
        Game playerGame = currentUser.getGame();

        vm.put("gameID", playerGame.getGameID());
        vm.put("currentUser", currentUser);
        // Needs to be non constant
        vm.put("viewMode", "PLAY"); 
        vm.put("redPlayer", playerGame.getPlayers()[0]);
        vm.put("whitePlayer", playerGame.getPlayers()[1]);
        // Needs to be non constant
        vm.put("activeColor", playerGame.getActiveColor()); 
        vm.put("board", playerGame.getBoardView(currentUser));

        // Check if the player's current game is inactive
        if(!playerGame.isActive()){
            // Tell the view that the game has ended
            Gson gson = new Gson();
            final Map<String, Object> modeOptions = new HashMap<>(2);
            modeOptions.put("isGameOver", true);
            modeOptions.put("gameOverMessage", playerGame.getGameEndedReason().getText());
            vm.put("modeOptionsAsJSON", gson.toJson(modeOptions));
        }

        // Render the View
        return templateEngine.render(new ModelAndView(vm , "game.ftl"));
    }
}
