package com.queueco.app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.client.WebSocketConnectionManager;

/**
 * The main entrypoint for teh program: defines the spring container and lets it
 * instantiate new beans while waiting for the connections to go through
 */
@ComponentScan
@PropertySource("classpath:application.properties")
@Component
public class App {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.queueco.app")) {
            Thread.currentThread().join();
            WebSocketConnectionManager wcm = (WebSocketConnectionManager) ctx.getBean("WebSocketConnectionManagerV1");

        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }
}
