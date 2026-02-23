package com.queueco.app.v2;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.client.WebSocketClient;
import org.springframework.web.socket.client.WebSocketConnectionManager;
import org.springframework.web.socket.client.standard.StandardWebSocketClient;

@Configuration
public class WebsocketClientConfig {
    @Bean
    WebSocketClient websocketClient() {
        return new StandardWebSocketClient();
    }

    @Bean("WebSocketConnectionManagerV2")
    WebSocketConnectionManager websocketManager(@Qualifier("HandlerV1") Handler handler,
            WebSocketClient websocketClient) {
        WebSocketConnectionManager webSocketManager = new WebSocketConnectionManager(websocketClient,
                handler,
                connectionURL);
        webSocketManager.setAutoStartup(true);
        return webSocketManager;
    }

    @Value("${connectionURL:wss://api.gemini.com/v2/marketdata}")
    private String connectionURL;

    public void connect() {
    }
}
