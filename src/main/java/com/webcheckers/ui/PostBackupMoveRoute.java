package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;
import com.webcheckers.model.Player;
import spark.*;

import java.util.Objects;
import java.util.logging.Logger;

import com.webcheckers.model.Game;
import com.webcheckers.model.Space;
import com.webcheckers.util.Message;

/**
 * the {@code POST /backupMove} route handler.
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 */
public class PostBackupMoveRoute implements Route {
    /**
     * Attributes
     *
     */
    private static final Logger LOG = Logger.getLogger(PostBackupMoveRoute.class.getName());
    static final String BACKUP_MOVE = "You moved a piece back.";

    /**
     * Constructor to create a {@link PostBackupMoveRoute}
     *
     * @param playerLobby  {@link PlayerLobby} object for extraction and manipulation of {@link Game} information
     */
    public PostBackupMoveRoute(PlayerLobby playerLobby) {
        Objects.requireNonNull(playerLobby, "playerLobby is required.");
        LOG.config("PostBackUpMoveRoute is initialized.");
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostBackupMoveRoute invoked");
        final Session httpSession = request.session();

        Player activePlayer = httpSession.attribute("currentUser");
        Game game = activePlayer.getGame();
        // Remove all pending boards and moves that were in use
        game.removeFromPendingMoves();
        game.removeFromPendingMoveBoards();

        // Update board
        Space[][] board;
        board = game.getLatestPendingMoveEndPositionBoard();
        game.modifyActualBoard(board);

        Message returnMessage = Message.info(BACKUP_MOVE);
        return returnMessage.toJson();
    }

}


