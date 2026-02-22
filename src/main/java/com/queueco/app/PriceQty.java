package com.queueco.app;

import java.math.BigDecimal;

public class PriceQty {
    private BigDecimal price, qty;

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public BigDecimal getQty() {
        return qty;
    }

    public void setQty(BigDecimal qty) {
        this.qty = qty;
    }

    public PriceQty() {
    }

    public PriceQty(BigDecimal price, BigDecimal qty) {
        this.price = price;
        this.qty = qty;
    }

}
