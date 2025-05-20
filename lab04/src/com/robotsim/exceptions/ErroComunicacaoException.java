package com.robotsim.exceptions;



public class ErroComunicacaoException extends Exception {
    public ErroComunicacaoException() {
        super("Ocorreu um erro na comunicação");
    }

    public ErroComunicacaoException(String message) {
        super(message);
    }

    public ErroComunicacaoException(String message, Throwable cause) {
        super(message, cause);
    }

    public ErroComunicacaoException(Throwable cause) {
        super(cause);
    }
}
