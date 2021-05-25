package com.webcheckers.ui;

import com.webcheckers.model.Player;
import spark.*;

import java.util.logging.Logger;

import com.webcheckers.model.Game;
import com.webcheckers.util.Message;

/**
 * the {@code POST /checkTurn} route handler.
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 */

public class PostCheckTurnRoute implements Route {
    /**
     * Attributes
     *
     */
    private static final Logger LOG = Logger.getLogger(PostCheckTurnRoute.class.getName());
    static final String TRUE = "true";
    static final String FALSE = "false";

    /**
     * Constructor to create a {@link PostCheckTurnRoute}
     */
    public PostCheckTurnRoute() {
        LOG.config("PostCheckTurnRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostCheckTurnRoute invoked");
        final Session httpSession = request.session();

        Player playerBeingChecked = httpSession.attribute("currentUser");
        Game game = playerBeingChecked.getGame();
        Player[] players = game.getPlayers();
        Player redPlayer = players[0];
        Player whitePlayer = players[1];
        Message returnMessage;

        // Check that the correct player's turn is being checked
        if(game.isRedPlayerTurn() && playerBeingChecked.getName().equals(redPlayer.getName())) {
            returnMessage = Message.info(TRUE);
        }
        else if(game.isWhitePlayerTurn() && playerBeingChecked.getName().equals(whitePlayer.getName())) {
            returnMessage = Message.info(TRUE);
        }
        else {
            returnMessage = Message.info(FALSE);
        }
        
        return returnMessage.toJson();
    }

}


