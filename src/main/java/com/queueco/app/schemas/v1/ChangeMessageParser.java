package com.queueco.app.schemas.v1;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.Iterator;

import org.springframework.stereotype.Component;

import com.queueco.app.PriceQty;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.core.JsonToken;
import tools.jackson.core.ObjectReadContext;
import tools.jackson.core.StreamReadFeature;
import tools.jackson.core.json.JsonFactory;

/**
 * Parses change messages from v1 API.
 * Ignores any messages which are not change based and logs them into an error
 * log
 */
@Component
public class ChangeMessageParser {
    private JsonFactory jsonFactory;
    private ArrayList<PriceQty> updates;

    /**
     * ChangeMessageParser Constructor
     *
     * @return ChangeMessageParser
     */
    public ChangeMessageParser() {
        this.jsonFactory = JsonFactory.builder()
                .enable(StreamReadFeature.INCLUDE_SOURCE_IN_LOCATION)
                .disable(StreamReadFeature.CLEAR_CURRENT_TOKEN_ON_CLOSE)
                .build();
        this.updates = new ArrayList<>();

    }

    static enum ParseState {
        SIDE, PRICE, QTY
    }

    /**
     * Parses a string using json parser
     * 
     * @param message a json message with price updates
     * @return iterator into an owned arraylist
     *
     */
    public Iterator<PriceQty> parseMessage(String message) {
        updates.clear();
        BigDecimal price = BigDecimal.ZERO, qty = BigDecimal.ZERO;
        boolean isAsk = false;
        EnumSet<ParseState> parseState = EnumSet.noneOf(ParseState.class);

        try (JsonParser parser = jsonFactory.createParser(ObjectReadContext.empty(), message)) {
            while (true) {
                if (parser.currentToken() == JsonToken.VALUE_STRING && parser.currentName().equals("price")) {
                    price = new BigDecimal(parser.getString());
                    parseState.add(ParseState.PRICE);
                } else if (parser.currentToken() == JsonToken.VALUE_STRING
                        && parser.currentName().equals("remaining")) {
                    qty = new BigDecimal(parser.getString());
                    parseState.add(ParseState.QTY);
                } else if (parser.currentToken() == JsonToken.VALUE_STRING && parser.currentName().equals("side")) {
                    if (parser.getString().equals("ask")) {
                        isAsk = true;
                    } else {
                        isAsk = false;
                    }
                    parseState.add(ParseState.SIDE);
                }
                if (parseState.size() == 3) {
                    updates.add(new PriceQty(price, qty, isAsk));
                    parseState = EnumSet.noneOf(ParseState.class);
                }
                if (parser.nextValue() == null) {
                    break;
                }
            }
        } catch (JacksonException _) {
        }
        return updates.iterator();
    }
}
