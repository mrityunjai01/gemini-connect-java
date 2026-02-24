package com.queueco.app;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class OrderBookTreeMap implements OrderBookInterface {
    // I can't use heaps because heaps (priority queues) don't support arbitrary
    // deletion
    private TreeMap<BigDecimal, BigDecimal> bids, asks;
    private Optional<PriceQty> best_bid, best_ask;
    private boolean consumable = false;

    /**
     * OrderBookTreeMap Constructor
     *
     * @return orderBook An OrderBookTreeMap instance
     */
    public OrderBookTreeMap() {
        this.bids = new TreeMap<>();
        this.asks = new TreeMap<>();
        this.best_bid = Optional.empty(); // our caller is responsible for checking for nulls
        this.best_ask = Optional.empty();
    }

    /**
     * Update a bid
     * 
     * @param price price
     * @param qty   size of the order
     *
     */
    public void updateBid(BigDecimal price, BigDecimal qty) { // the logic will be duplicated in updateAsk because I
        if (qty.compareTo(BigDecimal.ZERO) == 0) {
            this.bids.remove(price);
        } else {
            this.bids.put(price, qty);
        }

        if (this.best_bid.isEmpty() || price.compareTo(this.best_bid.get().getPrice()) >= 0) {
            Entry<BigDecimal, BigDecimal> bid = this.bids.pollLastEntry();
            if (this.best_bid.isPresent() && bid != null) {
                if (this.best_bid.get().getPrice().compareTo(bid.getKey()) < 0
                        || this.best_bid.get().getQty().compareTo(bid.getValue()) != 0) {
                    this.consumable = true;
                }
            } else {
                this.consumable = true;
            }
            if (bid != null) {

                this.best_bid = Optional.of(new PriceQty(bid.getKey(), bid.getValue(), false)); // cache the best bid
            } else {
                this.best_bid = Optional.empty();

            }
        }
    }

    /**
     * Update an Ask
     * 
     * @param price price
     * @param qty   size of the order
     *
     */
    public void updateAsk(BigDecimal price, BigDecimal qty) { // the logic will be duplicated in updateAsk because I
        if (qty.compareTo(BigDecimal.ZERO) == 0) {
            this.asks.remove(price);
        } else {
            this.asks.put(price, qty);
        }
        if (this.best_ask.isEmpty() || price.compareTo(this.best_ask.get().getPrice()) <= 0) {
            Entry<BigDecimal, BigDecimal> ask = this.asks.pollFirstEntry();
            if (this.best_ask.isPresent() && ask != null) {
                if (this.best_ask.get().getPrice().compareTo(ask.getKey()) > 0
                        || this.best_ask.get().getQty().compareTo(ask.getValue()) != 0) {
                    this.consumable = true;
                }
            } else {
                this.consumable = true;
            }
            if (ask != null) {
                this.best_ask = Optional.of(new PriceQty(ask.getKey(), ask.getValue(), true));
            } else {
                this.best_ask = Optional.empty();

            }
        }
    }

    /**
     * Consume a top of book price update
     * caller consumes a price update marking it stale
     * 
     * @return true if there's a new unconsumed price update
     *
     */
    public boolean consume_update() {
        if (this.consumable) {
            this.consumable = false;
            return true;
        }
        return false;
    }

    /**
     * Return the best bid on the book
     * 
     * @return Optional with price-quantity pair
     *
     */
    public Optional<PriceQty> getBestBid() {
        return best_bid;
    }

    /**
     * Return the best ask on the book
     * 
     * @return Optional with price-quantity pair
     *
     */
    public Optional<PriceQty> getBestAsk() {
        return best_ask;
    }

}
