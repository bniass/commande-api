package com.ecole221.commandeapi.exception;

public class PaiementException extends RuntimeException {
    public PaiementException(String message) {
        super(message);
    }

    public PaiementException(String message, Throwable cause) {
        super(message, cause);
    }
}
