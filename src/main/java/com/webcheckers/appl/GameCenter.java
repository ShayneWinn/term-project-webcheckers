package com.webcheckers.appl;

import java.util.HashMap;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;

/**
 * GameCenter, to create, store, and remove instances of games.
 *
 * @author <a href='mailto:amy5049@rit.edu'>Alexander Yu</a>
 * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
 * @author <a href="mailto:spm8848@rit.edu">Sean McDonnell</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class GameCenter {

    /**
     * List of all games
     */
    private HashMap<Integer, Game> manager = new HashMap<>();

    /**
     * Creates an instance of game and stores the game as a key inside of
     * manager hashmap.  Sets the value to the gameID.  (can be adjusted)
     *
     * @param redPlayer:player 1 (red)
     * @param whitePlayer:player 2 (white)
     */
    public void startGame(Player redPlayer, Player whitePlayer) {
        int gameID = manager.size() + 1;
        Game newGame = new Game(gameID, redPlayer, whitePlayer);
        manager.put(gameID, newGame);
        redPlayer.joinGame(newGame);
        whitePlayer.joinGame(newGame);
    }

    /**
     * Joins an instance of an active game
     * @param currentUser
     *      The {@link Player} requesting to spectate
     * @param requestedPlayer
     *      The {@link Player} that was requested to be spectated
     */
    public void spectateGame(Player currentUser, Player requestedPlayer) {
        currentUser.spectateGame(requestedPlayer.getGame());
    }

    /**
     * Removes a player from all games (if that player is in any games)
     *
     * @param player
     *      {@link Player} to remove
     */
    public void signOutPlayer(Player player) {
        if (player.inGame() && player.getSpectatorStatus() == false) {
            Game playerGame = player.getGame();
            if (playerGame.getPlayers()[0] == player) {
                playerGame.endGame(Game.Status.RED_ABANDONED);
            }
            else {
                playerGame.endGame(Game.Status.WHITE_ABANDONED);
            }
            playerGame.changeTurn();
        }
    }

    /**
     * Removes a player from all games (if that player is in any games), if that player chose to resign
     *
     * @param player
     *      {@link Player} to remove
     */
    public void resignPlayer(Player player)
    {
        if(player.inGame()) {
            Game playerGame = player.getGame();
            if(playerGame.getPlayers()[0] == player) {
                playerGame.endGame(Game.Status.RED_RESIGN);
            }
            else {
                playerGame.endGame(Game.Status.WHITE_RESIGN);
            }
            playerGame.changeTurn();
        }
    }

    /**
     * checks if a game is a real game that was created by the server
     *
     * @param game
     *   The {@link Game} to check
     * @return
     *   boolean showing if the game is real
     */
    public boolean isRealGame(Game game) {
        for(Game checkGame : manager.values()) {
            if(checkGame == game) {
                return true;
            }
        }
        return false;
    }
}
