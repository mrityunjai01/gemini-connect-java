package com.queueco.app;

import java.math.BigDecimal;
import java.util.Optional;

/**
 * Manages the order book state for a specific trading pair.
 * Handles bid/ask updates and ensures price integrity.
 */
public interface OrderBookInterface {
    /**
     * Update a bid
     * 
     * @param price price
     * @param qty   size of the order
     *
     */
    public void updateBid(BigDecimal price, BigDecimal qty);

    /**
     * Update an Ask
     * 
     * @param price price
     * @param qty   size of the order
     *
     */
    public void updateAsk(BigDecimal price, BigDecimal qty);

    /**
     * Consume a top of book price update
     * caller consumes a price update marking it stale
     * 
     * @return true if there's a new unconsumed price update
     *
     */
    public boolean consume_update();

    /**
     * Return the best bid on the book
     * 
     * @return Optional with price-quantity pair
     *
     */
    public Optional<PriceQty> getBestBid();

    /**
     * Return the best ask on the book
     * 
     * @return Optional with price-quantity pair
     *
     */
    public Optional<PriceQty> getBestAsk();
}
