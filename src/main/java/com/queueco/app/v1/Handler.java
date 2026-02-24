package com.queueco.app.v1;

import java.util.Iterator;
import java.util.Optional;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.queueco.app.OrderBookInterface;
import com.queueco.app.PriceQty;
import com.queueco.app.schemas.v1.ChangeMessageParser;

@Lazy
@Component("HandlerV1")
public class Handler extends TextWebSocketHandler {
    private OrderBookInterface orderBook;
    private ChangeMessageParser messageParser;

    /**
     * Handler Constructor
     *
     * @param orderBook An orderbook instance
     * @return A new handler
     */
    public Handler(OrderBookInterface orderBook, ChangeMessageParser changeMessageParser) {
        super();
        this.orderBook = orderBook;
        this.messageParser = changeMessageParser;
    }

    /**
     * Handle Text Messages, saving state in orderbook
     *
     * @param session websocket session
     * @param message message decoded from utf-8
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // the initial nmessage takes some time to process but after the initial one,
        // the updates are short messages
        Iterator<PriceQty> priceUpdates = this.messageParser.parseMessage(message.getPayload());
        while (priceUpdates.hasNext()) {
            PriceQty pq = priceUpdates.next();
            if (pq.isAsk()) {
                this.orderBook.updateAsk(pq.getPrice(), pq.getQty());
            } else {
                this.orderBook.updateBid(pq.getPrice(), pq.getQty());
            }
        }

        if (this.orderBook.consume_update()) {
            Optional<PriceQty> best_bid = orderBook.getBestBid();
            Optional<PriceQty> best_ask = orderBook.getBestAsk();

            if (best_bid.isPresent() && best_ask.isPresent()) {
                System.out.println(String.format("%s %s - %s %s", best_bid.get().getPrice(), best_bid.get().getQty(),
                        best_ask.get().getPrice(), best_ask.get().getQty()));

            }
        }

    }

}
