package com.webcheckers.ui;

import com.google.gson.Gson;
import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Move;
import com.webcheckers.model.Game;
import com.webcheckers.model.Space;
import com.webcheckers.util.Message;

/**
 * the {@code POST /validateMove} route handler.
 *
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 */

public class PostValidateMoveRoute implements Route {
    /**
     * Attributes
     *
     */
    private static final Logger LOG = Logger.getLogger(PostValidateMoveRoute.class.getName());

    public static final String ACTION_DATA_QUERY_PARAM = "actionData";
    public static final String CURRENT_USER_ATTR = "currentUser";
    public static final String OCCUPIED_SPACE = "You can't move to a space occupied by a piece.";
    public static final String NO_POSITION_PROVIDED_MESSAGE = "No position was provided for validation.";

    private final Gson gson;
    private final PlayerLobby playerLobby;

    /**
     * Constructor to create a {@link PostValidateMoveRoute}
     * @param gson  {@link Gson} object for generation of Json representations of Java objects
     * @param playerLobby  {@link PlayerLobby} object for extraction and manipulation of {@link Game} information
     */
    public PostValidateMoveRoute(Gson gson, PlayerLobby playerLobby) {
        this.gson = Objects.requireNonNull(gson, "gson is required.");
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");

        LOG.config("PostValidateMoveRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostValidateMoveRoute invoked");

        final Session httpSession = request.session();
        final String moveAsJson = request.queryParams(ACTION_DATA_QUERY_PARAM);

        Player activePlayer = httpSession.attribute(CURRENT_USER_ATTR);
        Game game = activePlayer.getGame();
        Space[][] actualBoard = game.getBoard();

        LOG.finest(String.format("JSON body: [%s]", moveAsJson));

        //Message stating if move is valid
        if(moveAsJson.contains("null")){
            return Message.error(OCCUPIED_SPACE).toJson();
        }
        else if(moveAsJson.isEmpty()) {
            return Message.error(NO_POSITION_PROVIDED_MESSAGE).toJson();
        }

        Move requestedMove = gson.fromJson(moveAsJson, Move.class);
        Space[][] boardCopy;

        if(game.hasBoards()) {
            boardCopy = game.createBoardCopy(game.getLatestPendingMoveEndPositionBoard());
        }
        else {
            boardCopy = game.createBoardCopy(actualBoard);
            game.addToPendingMoveBoards(actualBoard);
        }

        Message returnMessage = game.validateMove(requestedMove, boardCopy);
        return returnMessage.toJson();
    }

}


