package com.webcheckers.appl;

import com.webcheckers.model.Game;
import com.webcheckers.model.Player;
import com.webcheckers.util.Message;

import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * A class to create, destroy, and modify {@link Player} objects,
 * as well as handle their actions
 *
 * @author <a href='mailto:vm2961@rit.edu'>Vishnu Muthuswamy</a>
 * @author <a href='mailto:sdw5588@rit.edu'>Shayne Winn</a>
 * @author <a href='mailto:spm8848@rit.edu'>Sean McDonnell</a>
 * @author <a href='mailto:gjv9138@rit.edu'>Geran Vaughn</a>
 */
public class PlayerLobby {
    private static final Logger LOG = Logger.getLogger(PlayerLobby.class.getName());

    /** List of all players */
    private Map<String, Player> players = new HashMap<>();

    /** {@link GameCenter} used to handle game events. */
    private final GameCenter gameCenter;

    /**Patterns to validate usernames */
    private Pattern alphaNumericSpaceStrings = Pattern.compile("^[ a-zA-Z0-9]*$");
    private Pattern onlySpaceStrings = Pattern.compile("^[ ]*$");

    /**
     * Creates a {@link PlayerLobby} object that creates a {@link GameCenter} object
     */
    public PlayerLobby() {
        this.gameCenter = new GameCenter();
    }

    /**
     * Check the username of the player attempting to sign into the application and
     * allow them to sign into the player lobby
     *
     * @param player
     *   The {@link Player} entering the game for the first time
     * @return
     *   {@link Message} Stating if the player object has a valid username
     */
    public Message signIn(Player player) {
        if (isValid(player.getName())) {
            if (isAvailable(player.getName())) {
                this.players.put(player.getName(), player);
                return null;
            }
            return new Message("This username is already in use.", Message.Type.ERROR);
        }
        return new Message("This username is not valid.", Message.Type.ERROR);
    }

    /**
     * Lets the player sign out of the web application
     *
     * @param player
     *   {@link Player} attempting to signout of the game
     * @return
     *   The success of the player's signout request
     */
    public boolean signOut(Player player) {
        if (players.containsKey(player.getName())) {
            gameCenter.signOutPlayer(player);
            players.remove(player.getName());
            return true;
        }
        else {
            LOG.log(Level.SEVERE, "signOut() of player not in players list");
            return false;
        }
    }

    /**
     * checks that the player exists before the players game is resigned
     *
     * @param player
     * @return
     *      The resignation or failure of resignation
     */
    public boolean resignGame(Player player) {
        if (players.containsKey(player.getName())) {
            gameCenter.resignPlayer(player);
            return true;
        }
        else {
            LOG.log(Level.SEVERE, "resignGame() for player not in players list");
            return false;
        }
    }

    /**
     * Function to start a game
     *
     * @param currentUser
     *      The {@link Player} calling the functions
     * @param requestedUserName
     *      A {@link String} holding the username of the other user
     * @return
     *      {@link Message} if there is an error or null if no problem
     */
    public Message startGame(Player currentUser, String requestedUserName) {
        if (!this.players.containsValue(currentUser)) {
            return new Message("There was an issue with your request", Message.Type.ERROR);
        }
        Player requestedUser = findPlayer(requestedUserName);
        if (requestedUser == null) {
            return new Message("Failed to create a game with " + requestedUserName, Message.Type.ERROR);
        }
        if (requestedUser.inGame()) {
            return new Message("This player is already in a game!", Message.Type.ERROR);
        }
        this.gameCenter.startGame(currentUser, requestedUser);
        return null;
    }

    /**
     * Function to spectate a game
     *
     * @param currentUser
     *      The {@link Player} calling the functions
     * @param requestedUserName
     *      A {@link String} holding the username of the other user
     * @return
     *      {@link Message} if there is an error or null if no problem
     */
    public Message spectateGame(Player currentUser, String requestedUserName) {
        if (!this.players.containsValue(currentUser)) {
            return new Message("There was an issue with your request", Message.Type.ERROR);
        }
        Player requestedUser = findPlayer(requestedUserName);

        if (requestedUser == null) {
            return new Message("Failed to spectate " + requestedUserName, Message.Type.ERROR);
        }
        if (!requestedUser.inGame()) {
            return new Message("This player is no longer in a game!", Message.Type.ERROR);
        }
        this.gameCenter.spectateGame(currentUser, requestedUser);
        return null;
    }

    /**
     * Get the {@link Game} a player is currently in
     *
     * @param player
     *      {@link Player} to get the game of
     * @return
     *      {@link Game} if player is in a game, null if not
     */
    public boolean inRealGame(Player player){
        return gameCenter.isRealGame(player.getGame());
    }

    /**
     * Get the values of all {@link Player}s in the lobby
     *
     * @return
     *   A {@link Collection<Player>} representing all Players
     */
    public Collection<Player> getPlayers(){
        return players.values();
    }

    /**
     * Returns a list only containing the players not in a game
     *
     * @return
     *      {@link Collection<Player>} representing available players
     */
    public Collection<Player> getAvailablePlayers() {
        List<Player> availablePlayers = new ArrayList<>();
        for (Player player : players.values()) {
            if (!player.inGame()) {
                availablePlayers.add(player);
            }
        }
        return availablePlayers;
    }

    /**
     * Returns a list only containing the players in a game
     *
     * @return
     *      {@link Collection<Player>} representing players in game
     */
    public Collection<Player> getPlayersInGame() {
        List<Player> playersInGame = new ArrayList<>();
        for (Player player : players.values()) {
            if (player.inGame() && player.getSpectatorStatus() == false) {
                playersInGame.add(player);
            }
        }
        return playersInGame;
    }

    /**
     * Returns the count of currently online players
     *
     * @return
     *      integer representing number of players
     */
    public int getPlayerCount(){
        return players.size();
    }

    /**
     * Access {@link Player} object of a specific username
     *
     * @param userName
     *   Desired Player's username
     * @return
     *   Player Object associated with username
     */
    public Player findPlayer(String userName) {
        return players.get(userName);
    }

    /**
     * Check to see if the username is valid
     * Only contains alphanumeric characters and spaces
     * 
     * @param userName
     *   Player Username 
     * @return
     *   validity of username
     */
    public boolean isValid(String userName) {
        return userName != null && !userName.equals("") && !onlySpaceStrings.matcher(userName).find() &&
                alphaNumericSpaceStrings.matcher(userName).find();
    }

    /**
     * Check if a username is in the lobby and if the username is valid
     *
     * @param userName
     *   player's username
     * @return
     *   if the username was able to be verified
     */
    public boolean isAvailable(String userName) {
        return !players.containsKey(userName);
    }
}
