package com.queueco.app.schemas.v2;

public class InitialMessageSchema {
    private final String type;
    private final Subscription[] subscriptions;

    public InitialMessageSchema(String type, Subscription[] subscriptions) {
        this.type = type;
        this.subscriptions = subscriptions;
    }
}
