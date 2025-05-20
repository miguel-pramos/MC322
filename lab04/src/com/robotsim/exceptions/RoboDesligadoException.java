package com.robotsim.exceptions;

public class RoboDesligadoException extends Exception {
    public RoboDesligadoException() {
        super("O robô está desligado.");
    }

    public RoboDesligadoException(String message) {
        super(message);
    }
}
