package com.webcheckers.ui;

import com.webcheckers.appl.PlayerLobby;

import spark.Request;
import spark.Response;
import spark.Route;
import spark.Session;

import java.util.Objects;
import java.util.logging.Logger;

/**
 * the {@code POST /signout} route handler.
 *
 * @author <a href='sdw5588@rit.edu'>Shayne Winn</a>
 */
public class PostSignoutRoute implements Route {
    private static final Logger LOG = Logger.getLogger(PostSignoutRoute.class.getName());

    private final PlayerLobby playerLobby;

    /**
     * Create the Spark Route (UI controller) to handle all {@code POST /signin} HTTP requests.
     *
     * @param playerLobby
     *     the playerLobby to use to handle players
     */
    public PostSignoutRoute(PlayerLobby playerLobby){
        this.playerLobby = Objects.requireNonNull(playerLobby, "playerLobby is required.");
        LOG.config("PostSignoutRoute is initialized.");
    }

    
    /**
     * {@inheritDoc}
     */
    @Override
    public Object handle(Request request, Response response) {
        LOG.finer("PostSignoutRoute is invoked");

        final Session httpSession = request.session();
        // Check to see if session and current user exist
        if(httpSession != null){
            if(httpSession.attribute("currentUser") != null){
                // If both a session exists and there is a current user
                if(playerLobby.signOut(httpSession.attribute("currentUser"))){
                    httpSession.attribute("currentUser", null);
                }
            }
        }
        // Once the user signs out, redirect to the home page
        response.redirect("/");
        
        return null;
    }
}
