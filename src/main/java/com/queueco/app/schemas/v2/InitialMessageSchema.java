package com.queueco.app.schemas.v2;

public record InitialMessageSchema(String type, Subscription[] subscriptions) {
}
