package com.queueco.app;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

/**
 * The main entrypoint for teh program: defines the spring container and lets it
 * instantiate new beans while waiting for the connections to go through
 */
@ComponentScan
@EnableAutoConfiguration
public class App {
    public static void main(String[] args) {
        try (AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext("com.queueco.app")) {
            Thread.currentThread().join();

        } catch (Exception exception) {
            exception.printStackTrace();

        }

    }
}
