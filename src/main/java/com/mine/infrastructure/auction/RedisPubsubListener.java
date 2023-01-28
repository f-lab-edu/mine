package com.mine.infrastructure.auction;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.infrastructure.bid.RealtimeHighestBidResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.connection.MessageListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

import java.io.IOException;

@RequiredArgsConstructor
public class RedisPubsubListener implements MessageListener {

    private final SimpMessageSendingOperations template;
    private final ObjectMapper mapper;

    @Override
    public void onMessage(Message message, byte[] pattern) {
        try {
            RealtimeHighestBidResponse response = mapper.readValue(message.getBody(), RealtimeHighestBidResponse.class);
            template.convertAndSend("/topic/auction/" + response.getAuctionId(), response);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
