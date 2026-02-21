package com.queueco.app.v1;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import com.queueco.app.OrderBook;

@Component("HandlerV1")
public class Handler extends TextWebSocketHandler {
    private OrderBook orderBook;

    public Handler(OrderBook orderBook) {
        super();
        this.orderBook = orderBook;
    }

    @Override
    public void handleTextMessage(WebSocketSession session, TextMessage message) {
        System.out.println(message.getPayload());
        // the initial nmessage takes some time to process but after the initial one,
        // the updates are short messages

    }

}
