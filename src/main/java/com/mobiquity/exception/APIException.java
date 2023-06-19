package com.mobiquity.exception;

public class APIException extends Exception {

    public APIException(final String message, final Exception e) {
        super(message, e);
    }

    public APIException(final String message) {
        super(message);
    }
}
