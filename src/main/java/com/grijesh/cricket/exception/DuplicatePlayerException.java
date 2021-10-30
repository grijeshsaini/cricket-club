package com.grijesh.cricket.exception;

/**
 * Exception to throw when player with same name is already present.
 *
 * @author Grijesh Saini
 */
public class DuplicatePlayerException extends RuntimeException {

    public DuplicatePlayerException(String message) {
        super(message);
    }
}
