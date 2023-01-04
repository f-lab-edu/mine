package com.mine.domain.bid;

import com.mine.domain.auction.Auction;
import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class AutoBidRegisterCommand {

    private final long auctionId;
    private final long userId;
    private final long limit;

    public AutoBid toAutoBidEntity() {
        return AutoBid.builder()
                .auction(Auction.builder().id(this.auctionId).build())
                .user(User.builder().id(this.userId).build())
                .limit(this.limit)
                .build();
    }

    public AutoBidCache toAutoBidCacheEntity() {
        return AutoBidCache.builder()
                .auctionId(this.auctionId)
                .userId(this.userId)
                .limit(this.getLimit())
                .expiryTime(1)
                .build();
    }
}
