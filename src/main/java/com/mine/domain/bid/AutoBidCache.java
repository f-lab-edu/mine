package com.mine.domain.bid;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("auto")
@Getter
@Setter
@Builder
public class AutoBidCache {

    @Id
    private long auctionId;

    private long userId;

    private long limit;
}
