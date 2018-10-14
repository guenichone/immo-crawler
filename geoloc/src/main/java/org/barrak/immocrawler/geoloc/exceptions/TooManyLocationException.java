package org.barrak.immocrawler.geoloc.exceptions;

public class TooManyLocationException extends RuntimeException {
    public TooManyLocationException() {
    }

    public TooManyLocationException(String message) {
        super(message);
    }

    public TooManyLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
