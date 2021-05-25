package com.webcheckers.ui;

import spark.*;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * the {@code GET /spectator/stopWatching} route handler.
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class GetSpectatorStopWatchingRoute implements Route {
    private static final Logger LOG = Logger.getLogger(GetSpectatorStopWatchingRoute.class.getName());

    private final PlayerLobby playerLobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code GET /spectator/stopWatching} HTTP requests.
     *
     * @param playerLobby
     *     the {@link PlayerLobby} to use to create games; is not null
     */
    public GetSpectatorStopWatchingRoute (PlayerLobby playerLobby){
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");
        LOG.config("GetSpectatorStopWatchingRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("GetSpectatorStopWatchingRoute is invoked");

        final Session httpSession = request.session();

        // Check if the current session and current user exist
        if(httpSession == null || httpSession.attribute("currentUser") == null) {
            // If the user or session do not exist, redirect to the home page
            response.redirect("/");
            return null;
        }

        Player spectator = httpSession.attribute("currentUser");

        spectator.leaveGame();

        response.redirect("/");
        return null;
    }
}
