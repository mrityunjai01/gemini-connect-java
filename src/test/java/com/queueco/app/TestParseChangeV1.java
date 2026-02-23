package com.queueco.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.queueco.app.schemas.v1.ChangeMessageParser;

@DisplayName("Parse Change Messages in V1 Endpoint")
public class TestParseChangeV1 {
    ChangeMessageParser changeMessageParser;

    @BeforeEach
    public void setUp() {
        this.changeMessageParser = new ChangeMessageParser();
    }

    @Test
    public void parseChangeEmpty() {
        String changeMessage = "{}";
        Iterable<PriceQty> iterPQ = this.changeMessageParser.parseMessage(changeMessage);
        assertTrue(!iterPQ.iterator().hasNext());
    }

    @Test
    public void parseSingeChange() {
        String changeMessage = "{\"eventId\":1764526788507910,\"events\":[{\"delta\":\"-2.227868\",\"price\":\"68180.45\",\"reason\":\"cancel\",\"remaining\":\"0.0\",\"side\":\"bid\",\"type\":\"change\"}],\"socket_sequence\":13,\"timestamp\":1771708700,\"timestampms\":1771708700841,\"type\":\"update\"}";
        Iterable<PriceQty> iterPQ = this.changeMessageParser.parseMessage(changeMessage);
        assertTrue(iterPQ.iterator().hasNext());
        PriceQty pq = iterPQ.iterator().next();
        assertEquals(pq.getPrice(), new BigDecimal("68180.45"));
        assertEquals(pq.getQty(), new BigDecimal("0.0"));

        assertTrue(!iterPQ.iterator().hasNext());
    }

    @Test
    public void parseSingeChangeWithSpaces() {
        String changeMessage = "{\"eventId\":1764526788507910,       \"events\":      [     {\"delta\":\"-2.227868\",\"price\":\"68180.45\",                 \"reason\":\"cancel\",   \"remaining\":\"0.0\",\"side\":\"bid\",\"type\":\"change\"}],\"socket_sequence\":13,\"timestamp\":1771708700,\"timestampms\":1771708700841,\"type\":\"update\"}";
        Iterable<PriceQty> iterPQ = this.changeMessageParser.parseMessage(changeMessage);
        assertTrue(iterPQ.iterator().hasNext());
        PriceQty pq = iterPQ.iterator().next();
        assertEquals(pq.getPrice(), new BigDecimal("68180.45"));
        assertEquals(pq.getQty(), new BigDecimal("0.0"));

        assertTrue(!iterPQ.iterator().hasNext());
    }

    @Test
    public void parseMissingChanges() {
        String changeMessage = "{\"eventId\":1764526788507910,\"socket_sequence\":13,\"timestamp\":1771708700,\"timestampms\":1771708700841,\"type\":\"update\"}";
        Iterable<PriceQty> iterPQ = this.changeMessageParser.parseMessage(changeMessage);
        assertTrue(!iterPQ.iterator().hasNext());
    }

    @Test
    public void parseIncompleteChanges() {
        String changeMessage = "{\"eventId\":1764526788507910,\"events\":[{\"delta\":\"-2.227868\",\"price\":\"68180.45\",\"reason\":\"cancel\",\"side\":\"bid\",\"type\":\"change\"}],\"socket_sequence\":13,\"timestamp\":1771708700,\"timestampms\":1771708700841,\"type\":\"update\"}";
        Iterable<PriceQty> iterPQ = this.changeMessageParser.parseMessage(changeMessage);
        assertTrue(!iterPQ.iterator().hasNext());
    }

    @Test
    public void parseMultipleChanges() {
        String changeMessage = "{\"eventId\":1764526788507910,\"events\":[{\"delta\":\"-2.227868\",\"price\":\"68180.45\",\"reason\":\"cancel\",\"remaining\":\"0.0\",\"side\":\"bid\",\"type\":\"change\"}, {\"delta\":\"-2.227868\",\"price\":\"63180.45\",\"reason\":\"cancel\",\"remaining\":\"1.0\",\"side\":\"bid\",\"type\":\"change\"}],\"socket_sequence\":13,\"timestamp\":1771708700,\"timestampms\":1771708700841,\"type\":\"update\"}";
        Iterable<PriceQty> iterPQ = this.changeMessageParser.parseMessage(changeMessage);
        assertTrue(iterPQ.iterator().hasNext());
        PriceQty pq = iterPQ.iterator().next();
        assertEquals(pq.getPrice(), new BigDecimal("68180.45"));
        assertEquals(pq.getQty(), new BigDecimal("0.0"));
        assertTrue(iterPQ.iterator().hasNext());
        pq = iterPQ.iterator().next();
        assertEquals(pq.getPrice(), new BigDecimal("63180.45"));
        assertEquals(pq.getQty(), new BigDecimal("1.0"));
        assertTrue(!iterPQ.iterator().hasNext());
    }
}
