package com.robotsim.exceptions;

public class ColisaoException extends Exception {
    public ColisaoException() {
        super("Houve uma colisão");
    }

    public ColisaoException(String message) {
        super(message);
    }

    public ColisaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ColisaoException(Throwable cause) {
        super(cause);
    }
}
