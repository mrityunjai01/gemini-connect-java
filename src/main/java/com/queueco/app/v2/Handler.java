package com.queueco.app.v2;

import java.util.Iterator;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.BinaryMessage;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.queueco.app.OrderBook;
import com.queueco.app.PriceQty;
import com.queueco.app.schemas.v1.ChangeMessageParser;
import com.queueco.app.schemas.v2.InitialMessageSchema;
import com.queueco.app.schemas.v2.Subscription;

import tools.jackson.databind.ObjectMapper;

@Component("HandlerV2")
public class Handler extends TextWebSocketHandler {
    private OrderBook orderBook;

    public Handler(OrderBook orderBook) {
        super();
        this.orderBook = orderBook;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        // the initial nmessage takes some time to process but after the initial one,
        // the updates are short messages

    }

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
