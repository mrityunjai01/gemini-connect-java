package com.queueco.app.v2;

import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.queueco.app.OrderBookInterface;
import com.queueco.app.schemas.v2.InitialMessageSchema;
import com.queueco.app.schemas.v2.Subscription;

import tools.jackson.databind.ObjectMapper;

@Lazy
@Component("HandlerV2")
public class Handler extends TextWebSocketHandler {
    private OrderBookInterface orderBook;

    /**
     * Handler Constructor
     *
     * @param orderBook An orderbook instance
     * @return A new handler
     */
    public Handler(OrderBookInterface orderBook) {
        super();
        this.orderBook = orderBook;
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
        session.sendMessage(new BinaryMessage(objectMapper.writeValueAsBytes(initialMessage)));
        super.afterConnectionEstablished(session);
    }
}
