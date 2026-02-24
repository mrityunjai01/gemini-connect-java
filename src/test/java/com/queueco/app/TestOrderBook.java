package com.queueco.app;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("Parse Change Messages in V1 Endpoint")
public class TestOrderBook {
    OrderBookInterface orderBook;

    @BeforeEach
    public void setUp() {
        this.orderBook = new OrderBookTreeMap();
    }

    @Test
    public void emptyBook() {
        assertTrue(orderBook.getBestAsk().isEmpty());
        assertTrue(orderBook.getBestBid().isEmpty());
    }

    @Test
    public void singleUpdate() {
        assertTrue(!orderBook.consume_update());

        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("4.0"));

        assertTrue(orderBook.consume_update());

        assertTrue(orderBook.getBestAsk().isEmpty());
        assertTrue(!orderBook.getBestBid().isEmpty());

        assertEquals(orderBook.getBestBid().get().getPrice(), new BigDecimal("2.0"));
        assertEquals(orderBook.getBestBid().get().getQty(), new BigDecimal("4.0"));
    }

    @Test
    public void bidPriceImprovement() {
        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("4.0"));
        orderBook.consume_update();

        orderBook.updateBid(new BigDecimal("2.5"), new BigDecimal("1.0"));

        assertTrue(orderBook.consume_update());
        assertTrue(!orderBook.consume_update());
        assertEquals(new BigDecimal("2.5"), orderBook.getBestBid().get().getPrice());
        assertEquals(new BigDecimal("1.0"), orderBook.getBestBid().get().getQty());
    }

    @Test
    public void askPriceImprovement() {
        orderBook.updateAsk(new BigDecimal("2.0"), new BigDecimal("4.0"));
        orderBook.consume_update();

        orderBook.updateAsk(new BigDecimal("1.5"), new BigDecimal("3.0"));

        assertTrue(orderBook.consume_update());
        assertTrue(!orderBook.consume_update());
        assertEquals(new BigDecimal("1.5"), orderBook.getBestAsk().get().getPrice());
        assertEquals(new BigDecimal("3.0"), orderBook.getBestAsk().get().getQty());
    }

    @Test
    public void bidPriceNonImprovement() {
        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("4.0"));
        orderBook.consume_update();

        orderBook.updateBid(new BigDecimal("1.5"), new BigDecimal("1.0"));

        assertTrue(!orderBook.consume_update());
        assertEquals(new BigDecimal("2.0"), orderBook.getBestBid().get().getPrice());
        assertEquals(new BigDecimal("4.0"), orderBook.getBestBid().get().getQty());
    }

    @Test
    public void askPriceNonImprovement() {
        orderBook.updateAsk(new BigDecimal("2.0"), new BigDecimal("4.0"));
        orderBook.consume_update();

        orderBook.updateAsk(new BigDecimal("2.5"), new BigDecimal("1.0"));

        assertTrue(!orderBook.consume_update());
        assertEquals(new BigDecimal("2.0"), orderBook.getBestAsk().get().getPrice());
        assertEquals(new BigDecimal("4.0"), orderBook.getBestAsk().get().getQty());
    }

    @Test
    public void deletePriceLevel() {
        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("4.0"));
        assertTrue(orderBook.consume_update());

        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("0.0"));

        assertTrue(orderBook.consume_update());
        assertTrue(orderBook.getBestBid().isEmpty());
    }

    @Test
    public void deleteInnerLevelBid() {
        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("4.0"));
        orderBook.updateBid(new BigDecimal("2.1"), new BigDecimal("4.0"));
        assertTrue(orderBook.consume_update());

        orderBook.updateBid(new BigDecimal("2.0"), new BigDecimal("0.0"));

        assertTrue(!orderBook.consume_update());
        assertEquals(orderBook.getBestBid().get().getPrice(), new BigDecimal("2.1"));
    }

    @Test
    public void deleteInnerLevelAsk() {
        orderBook.updateAsk(new BigDecimal("2.0"), new BigDecimal("4.0"));
        orderBook.updateAsk(new BigDecimal("2.1"), new BigDecimal("4.0"));
        assertTrue(orderBook.consume_update());

        orderBook.updateAsk(new BigDecimal("2.0"), new BigDecimal("0.0"));

        assertTrue(!orderBook.consume_update());
        assertEquals(orderBook.getBestAsk().get().getPrice(), new BigDecimal("2.1"));
    }
}
