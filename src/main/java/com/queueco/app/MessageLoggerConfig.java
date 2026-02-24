package com.queueco.app;

import java.io.IOException;
import java.util.logging.Logger;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

@Configuration
public class MessageLoggerConfig {
    @Bean("missedMessagesLogger")
    @Lazy
    public Logger missedMessagesLogger() throws IOException {
        Handler fh = new FileHandler("missed_messages.log");
        Logger messages_logger = Logger.getLogger("com.queueco.app");
        messages_logger.setLevel(Level.SEVERE);
        messages_logger.addHandler(fh);
        return messages_logger;
    }
}
