package com.queueco.app.v2;

import java.util.Iterator;
import java.util.Optional;
import java.util.logging.Logger;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.queueco.app.OrderBookInterface;
import com.queueco.app.schemas.v2.ChangeSchema;
import com.queueco.app.schemas.v2.InitialMessageSchema;
import com.queueco.app.PriceQty;
import com.queueco.app.schemas.v2.Subscription;

import tools.jackson.databind.ObjectMapper;

@Lazy
@Component("HandlerV2")
public class Handler extends TextWebSocketHandler {
    private OrderBookInterface orderBook;
    private Logger logger;

    /**
     * Handler Constructor
     *
     * @param orderBook An orderbook instance
     * @return A new handler
     */
    public Handler(OrderBookInterface orderBook, Logger missedMessagesLogger) {
        super();
        this.orderBook = orderBook;
        this.logger = missedMessagesLogger;
    }

    /**
     * Handle Text Messages, saving state in orderbook
     *
     * @param session websocket session
     * @param message message decoded from utf-8
     */
    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // System.out.println("v2");
        // System.out.println(message.getPayload());

        // the initial nmessage takes some time to process but after the initial one,
        // the updates are short messages
        try {

            ChangeSchema changeSchema = new ChangeSchema(message.getPayload());
            Iterator<PriceQty> priceUpdates = changeSchema.getPriceUpdates().iterator();

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
                    System.out
                            .println(String.format("%s %s - %s %s", best_bid.get().getPrice(), best_bid.get().getQty(),
                                    best_ask.get().getPrice(), best_ask.get().getQty()));

                }
            }

        } catch (Exception e) {
            System.out.println(message.getPayload());

            this.logger.severe(message.getPayload());
        }
    }

    /**
     * Initial setup message
     *
     * @param session websocket session
     */
    @Override
    public void afterConnectionEstablished(WebSocketSession session) throws Exception {
        InitialMessageSchema initialMessage = new InitialMessageSchema(
                "subscribe", new Subscription[] {
                        new Subscription("l2", new String[] { "BTCUSD" }) });

        ObjectMapper objectMapper = new ObjectMapper();
        System.out.println("write initial message:");
        System.out.println(objectMapper.writeValueAsString(initialMessage));
        session.sendMessage(new BinaryMessage(objectMapper.writeValueAsBytes(initialMessage)));
        super.afterConnectionEstablished(session);
    }
}
