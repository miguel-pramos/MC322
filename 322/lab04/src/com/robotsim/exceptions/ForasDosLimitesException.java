package com.robotsim.exceptions;

public class ForasDosLimitesException extends Exception {
    public ForasDosLimitesException() {
        super("Houve uma colisão");
    }

    public ForasDosLimitesException(String message) {
        super(message);
    }

    public ForasDosLimitesException(String message, Throwable cause) {
        super(message, cause);
    }

    public ForasDosLimitesException(Throwable cause) {
        super(cause);
    }
}
