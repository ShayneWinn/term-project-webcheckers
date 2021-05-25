package com.webcheckers.ui;

import spark.*;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.util.Message;
import java.util.Objects;
import java.util.logging.Logger;

/**
 * the {@code POST /resignGame} route handler.
 *
 * @author <a href='spm8848@rit.edu'>Sean McDonnell </a>
 */
public class PostResignGameRoute implements Route{
    private static final Logger LOG = Logger.getLogger(PostResignGameRoute.class.getName());
    private static final String RESIGN_GAME = "The game has been resigned.";
    
    private final PlayerLobby playerLobby;
    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /resignGame} HTTP requests.
     *
     * @param playerLobby
     *     the {@link PlayerLobby} to use to create games; is not null
     */
    public PostResignGameRoute (PlayerLobby playerLobby){
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");
        LOG.config("PostResignGameRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostResignGameRoute is invoked");

        final Session httpSession = request.session();

        // Check to make sure a session exists 
        if(httpSession != null)
        {
            if(httpSession.attribute("currentUser") != null)
            {
                playerLobby.resignGame(httpSession.attribute("currentUser"));
            }
        }

        // Return message stating the game was resigned 
        Message returnMessage = Message.info(RESIGN_GAME);
        return returnMessage.toJson();
    }
    
}
