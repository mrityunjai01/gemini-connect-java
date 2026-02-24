package com.queueco.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Iterator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.queueco.app.schemas.v2.ChangeSchema;

import tools.jackson.databind.ObjectMapper;

@DisplayName("Parse Change Messages in V1 Endpoint")
public class TestParseChangeV2 {
    private ObjectMapper om;

    @BeforeEach
    public void setUp() {
        om = new ObjectMapper();
    }

    @Test
    public void parseChangeEmpty() throws Exception {
        String changeMessage = "{}";
        ChangeSchema changeSchema = new ChangeSchema(changeMessage);
        Iterator<PriceQty> iterPQ = changeSchema.getPriceUpdates().iterator();
        assertTrue(!iterPQ.hasNext());
    }

    @Test
    public void parseSingeChange() throws Exception {
        String changeMessage = "{\"changes\":[[\"buy\",\"64443.0\",\"0.4\"]]}";

        ChangeSchema changeSchema = new ChangeSchema(changeMessage);
        Iterator<PriceQty> iterPQ = changeSchema.getPriceUpdates().iterator();

        assertTrue(iterPQ.hasNext());
        PriceQty pq = iterPQ.next();
        assertEquals(pq.getPrice(), new BigDecimal("64443.0"));
        assertEquals(pq.getQty(), new BigDecimal("0.4"));
        assertTrue(!pq.isAsk());
        assertTrue(!iterPQ.hasNext());
    }

    @Test
    public void parseSingeChangeExtraFields() throws Exception {
        String changeMessage = "{\"type\": \"sub\", \"changes\":[[\"buy\",\"64443.0\",\"0.4\"]]}";

        ChangeSchema changeSchema = new ChangeSchema(changeMessage);
        Iterator<PriceQty> iterPQ = changeSchema.getPriceUpdates().iterator();

        assertTrue(iterPQ.hasNext());
        PriceQty pq = iterPQ.next();
        assertEquals(pq.getPrice(), new BigDecimal("64443.0"));
        assertEquals(pq.getQty(), new BigDecimal("0.4"));
        assertTrue(!pq.isAsk());
        assertTrue(!iterPQ.hasNext());
    }

    @Test
    public void parseSingeChangeWithSpaces() throws Exception {
        String changeMessage = "{ \"changes\" :[ [ \"buy\", \"64443.0\", \"0.4\"]]}";

        ChangeSchema changeSchema = new ChangeSchema(changeMessage);
        Iterator<PriceQty> iterPQ = changeSchema.getPriceUpdates().iterator();

        assertTrue(iterPQ.hasNext());
        PriceQty pq = iterPQ.next();
        assertEquals(pq.getPrice(), new BigDecimal("64443.0"));
        assertEquals(pq.getQty(), new BigDecimal("0.4"));
        assertTrue(!pq.isAsk());
        assertTrue(!iterPQ.hasNext());
    }

    @Test
    public void parseMultipleChanges() throws Exception {
        String changeMessage = "{\"changes\":[[\"buy\",\"64443.0\",\"0.4\"], [\"sell\", \"3233.0\", \"0.3\"]]}";

        ChangeSchema changeSchema = new ChangeSchema(changeMessage);
        Iterator<PriceQty> iterPQ = changeSchema.getPriceUpdates().iterator();

        assertTrue(iterPQ.hasNext());
        PriceQty pq = iterPQ.next();
        assertEquals(pq.getPrice(), new BigDecimal("64443.0"));
        assertEquals(pq.getQty(), new BigDecimal("0.4"));
        assertTrue(!pq.isAsk());
        assertTrue(iterPQ.hasNext());
        pq = iterPQ.next();
        assertEquals(pq.getPrice(), new BigDecimal("3233.0"));
        assertEquals(pq.getQty(), new BigDecimal("0.3"));
        assertTrue(pq.isAsk());
    }

}
