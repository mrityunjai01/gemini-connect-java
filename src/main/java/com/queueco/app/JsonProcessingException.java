package com.queueco.app;

/**
 * Exception thrown if cusotm handling of json string throws up
 * Used to recover from syntactically broken websocket messages
 */
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
