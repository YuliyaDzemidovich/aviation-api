package com.github.yuliyadzemidovich.aviationapi.exception;

public class UpstreamUnavailableException extends AviationApiException {

    public UpstreamUnavailableException(String message) {
        super(message);
    }

    public UpstreamUnavailableException(String message, Throwable cause) {
        super(message, cause);
    }
}
