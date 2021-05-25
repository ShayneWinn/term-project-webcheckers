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
import java.util.logging.Logger;

/**
 * the {@code POST /signin} route handler.
 *
 * @author <a href='sdw5588@rit.edu'>Shayne Winn</a>
 */
public class PostSigninRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSigninRoute.class.getName());

    public static final String CURRENT_USER_ATTR = "currentUser";
    public static final String RETURN_MESSAGE_ATTR = "message";
    public static final String SIGN_IN_VIEW_NAME = "signin.ftl";
    private final TemplateEngine templateEngine;
    private final PlayerLobby playerLobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signin} HTTP requests.
     *
     * @param templateEngine
     *     the HTML template rendering engine
     */
    public PostSigninRoute(TemplateEngine templateEngine, PlayerLobby playerLobby){
        this.templateEngine = Objects.requireNonNull(templateEngine, "templateEngine is required");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");
        LOG.config("PostSigninRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSigninRoute is invoked.");
        final Session httpSession = request.session();
        Map<String, Object> vm = new HashMap<>();

        // Get the parameter in the form
        String userName = request.queryParams(CURRENT_USER_ATTR);

        Player newPlayer = new Player(userName);
        Message returnMessage = playerLobby.signIn(newPlayer);

        if(returnMessage != null){
            // Display error message if one occurs
            vm.put(RETURN_MESSAGE_ATTR, returnMessage);
            return templateEngine.render(new ModelAndView(vm, SIGN_IN_VIEW_NAME));
        }
        else {
            httpSession.attribute(CURRENT_USER_ATTR, newPlayer);
            // Send the user back home with their new attribute
            response.redirect("/");
            return null;
        }
    }
}
