package com.queueco.app.schemas.v2;

public class Subscription {
    private String name;
    private String[] symbols;

    public Subscription(String name, String[] symbols) {
        this.name = name;
        this.symbols = symbols;
    }

}
