package com.queueco.app;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map.Entry;

import org.springframework.stereotype.Component;

@Component
public class OrderBook {
    // I can't use heaps because heaps (priority queues) don't support arbitrary
    // deletion
    private TreeMap<BigDecimal, BigDecimal> bids, asks;
    private Optional<PriceQty> best_bid, best_ask;

    public OrderBook() {
        this.bids = new TreeMap<>();
        this.asks = new TreeMap<>();
        this.best_bid = Optional.empty(); // our caller is responsible for checking for nulls
        this.best_ask = Optional.empty();
    }

    public void updateBid(BigDecimal price, BigDecimal qty) { // the logic will be duplicated in updateAsk because I
        if (qty == BigDecimal.ZERO) {
            this.bids.remove(price);
        } else {
            this.bids.put(price, qty);
            if (this.best_bid.isEmpty() || price.compareTo(this.best_bid.get().getPrice()) >= 0) {
                Entry<BigDecimal, BigDecimal> bid = this.bids.pollLastEntry();
                this.best_bid = Optional.of(new PriceQty(bid.getKey(), bid.getValue(), false)); // cache the best bid
            }
        }

    }

    public void updateAsk(BigDecimal price, BigDecimal qty) { // the logic will be duplicated in updateAsk because I
        if (qty == BigDecimal.ZERO) {
            this.asks.remove(price);
        } else {
            this.asks.put(price, qty);
            if (this.best_ask.isEmpty() || price.compareTo(this.best_ask.get().getPrice()) <= 0) {
                Entry<BigDecimal, BigDecimal> ask = this.asks.pollFirstEntry();
                this.best_ask = Optional.of(new PriceQty(ask.getKey(), ask.getValue(), false));
            }
        }
    }

    public Optional<PriceQty> getBestBid() {
        return best_bid;
    }

    public Optional<PriceQty> getBestAsk() {
        return best_ask;
    }

}
