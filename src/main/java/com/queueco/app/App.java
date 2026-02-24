package com.queueco.app;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

@ComponentScan
public class App {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(
                "com.queueco.app")) {
            Object webSocketManager = ctx.getBean("WebSocketConnectionManagerV1");
            Thread.currentThread().join();

        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }
}
