package com.webcheckers.model;

/**
 * Model tier component that holds a player's information.
 *
 * @author <a href='vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class Player {

    /**
     * {@link String} representing the player's name
     */
    private final String name;

    private boolean inGame;
    private boolean isSpectator;
    private String spectateTurn;
    private Game currentGame;

    /**
     * Create {@link Player} instance to hold information of a player.
     *
     * @param userName
     *     username of {@link Player}
     */
    public Player(String userName) {
        this.name = userName;
    }

    /**
     * Get username of {@link Player}.
     *
     * @return
     *   username of {@link Player}
     */
    public String getName() {
        return name;
    }

    /**
     * Is the player in a game?
     *
     * @return
     *   boolean, is player in game?
     *
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    public boolean inGame(){
        return this.inGame;
    }

    /**
     * determines if the players {@link Game} is active
     *
     * @return
     *   is the {@link Game} active
     *
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    public boolean isGameActive() {
        if (currentGame != null) {
            return currentGame.isActive();
        }
        else {
            return false;
        }
    }

    /**
     * tells the player to join a given {@linkplain Game}game
     *
     * @param game
     *   the {@link Game} to join
     *
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    public void joinGame(Game game) {
        this.inGame = true;
        currentGame = game;
    }

    /**
     * tells the player to join a given {@linkplain Game}game
     *
     * @param game
     *   the {@link Game} to join
     */
    public void spectateGame(Game game) {
        this.inGame = true;
        this.isSpectator = true;
        this.spectateTurn = game.getActiveColor();
        currentGame = game;
    }

    /**
     * Checks if the turn has progressed without the spectator knowing
     * @return
     *      true if the state has updated, false if the game has not
     */
    public boolean spectateDidTurnUpdate(){
        if(spectateTurn.equals(currentGame.getActiveColor())){
            return false;
        }
        else{
            spectateTurn = currentGame.getActiveColor();
            return true;
        }
    }

    /**
     * tells the player to leave their current {@linkplain Game}game
     *
     * @author <a href="mailto:sdw5588@rit.edu">Shayne Winn</a>
     */
    public void leaveGame() {
        this.inGame = false;
        this.isSpectator = false;
        currentGame = null;
    }

    /**
     * Get the Player's current {@linkplain Game}game.
     *
     * @return
     *  the {@link Game} the player is currently in, or null if not in a game
     */
    public Game getGame() {
        if(currentGame != null) {
            return currentGame;
        }
        return null;
    }

    /**
     * Get the {@link Player}'s spectator status
     * @return
     *      a boolean, true for spectating, false for not spectating
     */
    public boolean getSpectatorStatus() {
        return isSpectator;
    }
}
