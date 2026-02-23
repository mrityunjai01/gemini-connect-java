package com.queueco.app;

public class JsonProcessingException extends Exception {
    public JsonProcessingException() {
        super();
    }

    public JsonProcessingException(String jsonString) {
        super("Exception thrown processing: " + jsonString);
    }

    public JsonProcessingException(String message, Throwable cause) {
        super(message, cause);
    }

    public JsonProcessingException(Throwable cause) {
        super(cause);
    }
}
