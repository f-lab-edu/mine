package com.mine.domain.bid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

import java.util.concurrent.TimeUnit;

@RedisHash("auto_bid")
@Getter
@Setter
@Builder
public class AutoBidCache {

    @Id
    private long auctionId;

    private long userId;

    private long limit;

    @TimeToLive(unit = TimeUnit.DAYS)
    private int expiryTime;
}
