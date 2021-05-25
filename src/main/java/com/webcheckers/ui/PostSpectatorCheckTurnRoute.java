package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.*;

import java.util.logging.Logger;

import com.webcheckers.util.Message;

/**
 * the {@code POST /spectator/checkTurn} route handler.
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 */
public class PostSpectatorCheckTurnRoute implements Route {
    /**
     * Attributes
     */
    private static final Logger LOG = Logger.getLogger(PostSpectatorCheckTurnRoute.class.getName());
    static final String TRUE = "true";
    static final String FALSE = "false";

    /**
     * Constructor to create a {@link PostSpectatorCheckTurnRoute}
     */
    public PostSpectatorCheckTurnRoute() {
        LOG.config("PostSpectatorCheckTurnRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSpectatorCheckTurnRoute invoked");
        final Session httpSession = request.session();

        Player spectator = httpSession.attribute("currentUser");
        Message returnMessage;

        // Check if the turn has changed
        if(spectator.spectateDidTurnUpdate()){
            returnMessage = Message.info(TRUE);
        }
        else{
            returnMessage = Message.info(FALSE);
        }

        return returnMessage.toJson();
    }
}
