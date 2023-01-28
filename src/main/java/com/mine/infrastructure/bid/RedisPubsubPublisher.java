package com.mine.infrastructure.bid;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mine.domain.bid.HighestBid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisPubsubPublisher {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper mapper;

    public void convertAndSend(HighestBid highestBid) {
        RealtimeHighestBidResponse realtimeHighestBid = RealtimeHighestBidResponse.builder()
                .auctionId(highestBid.getAuction().getId())
                .highestBidAmount(highestBid.getHighestBidAmount())
                .incrementalBidAmount(highestBid.getIncrementalBidAmount())
                .build();

        try {
            String message = mapper.writeValueAsString(realtimeHighestBid);
            redisTemplate.convertAndSend("highest-bid", message);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
