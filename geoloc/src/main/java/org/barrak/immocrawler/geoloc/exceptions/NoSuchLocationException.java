package org.barrak.immocrawler.geoloc.exceptions;

public class NoSuchLocationException extends RuntimeException {
    public NoSuchLocationException() {
    }

    public NoSuchLocationException(String message) {
        super(message);
    }

    public NoSuchLocationException(String message, Throwable cause) {
        super(message, cause);
    }
}
