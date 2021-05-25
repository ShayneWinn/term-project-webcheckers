package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.TemplateEngine;
import spark.Session;
import spark.ModelAndView;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The UI Controller to GET the Home page.
 *
 * @author <a href='mailto:bdbvse@rit.edu'>Bryan Basham</a>
 * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
 */
public class GetHomeRoute implements Route {
  private static final Logger LOG = Logger.getLogger(GetHomeRoute.class.getName());

  public static final String HOME_ROUTE = "/";
  public static final String CURRENT_USER_ATTR = "currentUser";
  public static final String MSG_ATTR = "message";
  public static final String TITLE_ATTR = "title";
  public static final String TITLE = "Welcome!";
  public static final String REDIRECT_MESSAGE_PARAM = "redirectMessage";
  public static final String ERROR_STRING = "ERROR";
  public static final String REDIRECT_MESSAGE_TYPE_PARAM = "t";
  public static final String AVAILABLE_PLAYERS_ATTR = "availablePlayers";
  public static final String PLAYERS_IN_GAME = "playersInGame";
  public static final String PLAYERS_ONLINE_ATTR = "playersOnline";
  public static final String VIEW_NAME = "home.ftl";
  public static final Message WELCOME_MSG = Message.info("Welcome to the world of online Checkers.");

  private final TemplateEngine templateEngine;
  private final PlayerLobby playerLobby;

  /**
   * Create the Spark Route (UI controller) to handle all {@code GET /} HTTP requests.
   *
   * @param templateEngine
   *   the HTML template rendering engine
   */
  public GetHomeRoute(final TemplateEngine templateEngine, final PlayerLobby playerLobby) {
    this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
    this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");
    LOG.config("GetHomeRoute is initialized.");
  }

  /**
   * Render the WebCheckers Home page.
   *
   * @param request
   *   the HTTP request
   * @param response
   *   the HTTP response
   *
   * @return
   *   the rendered HTML for the Home page
   */
  @Override
  public Object handle(Request request, Response response) {
    LOG.finer("GetHomeRoute is invoked.");
    final Session httpSession = request.session();

    Player currentUser = httpSession.attribute("currentUser");
    // Check if the user is in a game
    if(currentUser != null) {
      if(currentUser.inGame()) {
        // Make sure the game is real and still exists
        if (playerLobby.inRealGame(currentUser)) {
          if (currentUser.isGameActive() && currentUser.getSpectatorStatus() == false) {
            // The user is in a currently active game
            response.redirect("/game");
            return null;
          }
          else if(currentUser.isGameActive() && currentUser.getSpectatorStatus() == true) {
            // The user is currently spectating a game
            response.redirect("/spectator/game");
          }
          else {
            // Player is in an inactive game
            currentUser.leaveGame();
          }
        }
        else {
          // If the user is in a non-real game make them leave
          LOG.log(Level.SEVERE, currentUser.getName() + " found in non-real game.");
          currentUser.leaveGame();
        }
      }
    }

    // Create hash map to use for messages
    Map<String, Object> vm = new HashMap<>();
    vm.put(TITLE_ATTR, TITLE);

    if(request.queryParams(REDIRECT_MESSAGE_PARAM) != null) {
      Message.Type type;
      if (ERROR_STRING.equals(request.queryParams(REDIRECT_MESSAGE_TYPE_PARAM))) {
        type = Message.Type.ERROR;
      } 
      else {
        type = Message.Type.INFO;
      }

      vm.put(MSG_ATTR, new Message(request.queryParams(REDIRECT_MESSAGE_PARAM), type));
    }
    else {
      // Display a user message in the home page
      vm.put(MSG_ATTR, WELCOME_MSG);
    }

    Collection<Player> availablePlayers = playerLobby.getAvailablePlayers();
    Collection<Player> playersInGame = playerLobby.getPlayersInGame();
    vm.put(CURRENT_USER_ATTR, currentUser);
    vm.put(AVAILABLE_PLAYERS_ATTR, availablePlayers);
    vm.put(PLAYERS_IN_GAME, playersInGame);
    vm.put(PLAYERS_ONLINE_ATTR, playerLobby.getPlayerCount());

    // Render the View
    return templateEngine.render(new ModelAndView(vm , VIEW_NAME));
  }
}
