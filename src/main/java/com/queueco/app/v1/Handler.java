package com.queueco.app.v1;

import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.queueco.app.OrderBook;
import com.queueco.app.PriceQty;
import com.queueco.app.schemas.v1.ChangeMessageParser;

@Component("HandlerV1")
public class Handler extends TextWebSocketHandler {
    private OrderBook orderBook;
    private ChangeMessageParser messageParser;

    public Handler(OrderBook orderBook, ChangeMessageParser changeMessageParser) {
        super();
        this.orderBook = orderBook;
        this.messageParser = changeMessageParser;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        Iterable<PriceQty> priceUpdates = this.messageParser.parseMessage(message.getPayload());
        for (PriceQty pq : priceUpdates) {
            if (pq.isAsk()) {
                orderBook.updateAsk(pq.getPrice(), pq.getQty());
            } else {
                orderBook.updateBid(pq.getPrice(), pq.getQty());
            }
        }

        Optional<PriceQty> best_bid = orderBook.getBestBid();
        Optional<PriceQty> best_ask = orderBook.getBestAsk();

        if (best_bid.isPresent() && best_ask.isPresent()) {
            System.out.println(String.format("%s %s - %s %s", best_bid.get().getPrice(), best_bid.get().getQty(),
                    best_ask.get().getPrice(), best_ask.get().getQty()));

        }
        // the initial nmessage takes some time to process but after the initial one,
        // the updates are short messages

    }

}
