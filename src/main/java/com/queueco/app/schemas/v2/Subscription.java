package com.queueco.app.schemas.v2;

public class Subscription {
    private String name;
    private String[] symbols;

    public Subscription(String name, String[] symbols) {
        this.name = name;
        this.symbols = symbols;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String[] getSymbols() {
        return symbols;
    }

    public void setSymbols(String[] symbols) {
        this.symbols = symbols;
    }

}
