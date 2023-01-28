package com.mine.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.listener.PatternTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

@Configuration
@RequiredArgsConstructor
public class RedisPubsubConfig {

    private final SimpMessageSendingOperations template;
    private final ObjectMapper mapper;
    private final RedisConnectionFactory connectionFactory;

    @Bean
    public MessageListenerAdapter redisPubsubListenerAdapter() {
        return new MessageListenerAdapter(new RedisPubsubListener(template, mapper));
    }

    @Bean
    public RedisMessageListenerContainer redisPubsubContainer() {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.addMessageListener(redisPubsubListenerAdapter(), new PatternTopic("highest-bid"));
        return container;
    }
}
