package com.queueco.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.queueco.app.v1.Handler;
import com.queueco.app.v1.WebsocketClientConfig;

@ComponentScan
public class App {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                WebsocketClientConfig.class, Handler.class)) {

            Object webSocketManager = ctx.getBean("WebSocketConnectionManagerV1");
            Thread.currentThread().join();

        } catch (Exception exception) {

        }

    }
}
