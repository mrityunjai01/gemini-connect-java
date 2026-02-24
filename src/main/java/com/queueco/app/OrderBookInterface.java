package com.queueco.app;

import java.math.BigDecimal;
import java.util.Optional;

public interface OrderBookInterface {
    public void updateBid(BigDecimal price, BigDecimal qty);

    public void updateAsk(BigDecimal price, BigDecimal qty);

    public boolean consume_update();

    public Optional<PriceQty> getBestBid();

    public Optional<PriceQty> getBestAsk();
}
