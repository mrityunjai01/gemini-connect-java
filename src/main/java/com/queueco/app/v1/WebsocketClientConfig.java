package com.queueco.app.v1;

import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@PropertySource("classpath:application.properties")
@ConditionalOnProperty(name = "v1.enabled", havingValue = "true")
@Configuration("WebSocketClientConfigV1")
@Lazy
public class WebsocketClientConfig {
    @Bean("WebsocketClientV1")
    @Lazy
    WebSocketClient websocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean("WebSocketConnectionManagerV1")
    @Lazy
    WebSocketConnectionManager websocketManager(@Qualifier("HandlerV1") Handler handler,
            @Qualifier("WebsocketClientV1") WebSocketClient websocketClient, Logger missedMessagesLogger) {
        WebSocketConnectionManager webSocketManager = new WebSocketConnectionManager(websocketClient,
                handler,
                connectionURL);
        webSocketManager.setAutoStartup(true);
        return webSocketManager;
    }

    @Value("${v1connectionURL:wss://api.gemini.com/v1/marketdata/BTCUSD}")
    private String connectionURL;

    public void connect() {
    }
}
