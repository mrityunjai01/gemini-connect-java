package com.queueco.app.v2;

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
@ConditionalOnProperty(name = "v2.enabled", havingValue = "true")
@Configuration("WebSocketClientConfigV2")
@Lazy
public class WebsocketClientConfig {
    @Bean("WebsocketClientV2")
    @Lazy
    WebSocketClient websocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean("WebSocketConnectionManagerV2")
    @Lazy
    WebSocketConnectionManager websocketManager(@Qualifier("HandlerV2") Handler handler,
            @Qualifier("WebsocketClientV2") WebSocketClient websocketClient, Logger missedMessagesLogger) {
        WebSocketConnectionManager webSocketManager = new WebSocketConnectionManager(websocketClient,
                handler,
                connectionURL);
        webSocketManager.setAutoStartup(true);
        return webSocketManager;
    }

    @Value("${v2connectionURL:wss://api.gemini.com/v2/marketdata}")
    private String connectionURL;

    public void connect() {
    }
}
