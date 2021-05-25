package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * the {@code POST /spectator/game} route handler.
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='sdw5588@rit.edu'>Shayne Winn</a>
 */
public class PostSpectateGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostSpectateGameRoute.class.getName());
    public static final String OTHER_USER_QUERY = "otherUser";

    private final PlayerLobby playerLobby;

    /**
     * Constructor to create a {@link PostSpectateGameRoute}
     *
     * @param playerLobby
     *      the {@link PlayerLobby} to use to create games; is not null
     */
    public PostSpectateGameRoute(PlayerLobby playerLobby){
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");
        LOG.config("PostSpectateGameRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        final Session httpSession = request.session();

        // Check the session to make sure it is not new and has the current user
        if(httpSession == null || httpSession.attribute(GetHomeRoute.CURRENT_USER_ATTR) == null){
            response.redirect(GetHomeRoute.HOME_ROUTE);
            return null;
        }

        // Check that the current user is signed-in
        if(!playerLobby.getPlayers().contains(httpSession.attribute(GetHomeRoute.CURRENT_USER_ATTR))) {
            response.redirect(GetHomeRoute.HOME_ROUTE);
        }

        String otherUserName = request.queryParams(OTHER_USER_QUERY);
        Player currentUser = httpSession.attribute(GetHomeRoute.CURRENT_USER_ATTR);
        Message returnMessage = this.playerLobby.spectateGame(currentUser, otherUserName);

        // Check to see if there was an issue with spectating the game
        if(returnMessage == null){
            response.redirect(GetSpectateGameRoute.SPECTATOR_ROUTE);
        }
        // If there was an issue, redirect back to the home page
        else{
            response.redirect(GetHomeRoute.HOME_ROUTE + "?redirectMessage=" + returnMessage.getText() +
                    "&t=" + returnMessage.getType().toString());
        }
        return null;
    }
}