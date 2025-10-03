package com.github.yuliyadzemidovich.aviationapi.exception;

/**
 * Base custom exception for this app
 */
public class AviationApiException extends RuntimeException {

    public AviationApiException(String message) {
        super(message);
    }

    public AviationApiException(String message, Throwable cause) {
        super(message, cause);
    }
}
