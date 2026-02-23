package com.queueco.app.schemas.v1;

import java.util.ArrayList;

import com.queueco.app.PriceQty;

import tools.jackson.core.json.JsonFactory;

public class ChangeMessageParser {
    private ArrayList<PriceQty> pqArray;
    private JsonFactory jsonFactory;

    public ChangeMessageParser() {
        this.jsonFactory = new JsonFactory();
        this.pqArray = new ArrayList<>(1000);
    }

    public Iterable<PriceQty> parseMessage(String message) {
        return pqArray;
    }
}
