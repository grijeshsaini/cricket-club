package com.grijesh.cricket.exception;

/**
 * Exception to throw when player not found.
 *
 * @author Grijesh Saini
 */
public class PlayerNotFoundException extends RuntimeException {

    public PlayerNotFoundException(String message) {
        super(message);
    }
}
