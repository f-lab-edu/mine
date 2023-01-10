package com.mine.domain.bid;

import com.mine.domain.auction.Auction;
import com.mine.domain.user.User;
import com.mine.util.IncrementUtil;
import lombok.RequiredArgsConstructor;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RequiredArgsConstructor
public abstract class AbstractBid {

    protected final BidHistoryStore bidHistoryStore;
    ZonedDateTime biddingTime = ZonedDateTime.now(ZoneId.of("UTC"));

    protected BidHistory bid(long amount, long auctionId, long userId, HighestBid highestBid) {
        BidHistory bidHistory = this.recordBidHistory(auctionId, userId, amount);
        Long incrementalBidAmount = this.calcIncrementalBidAmount(bidHistory.getBiddingAmount());
        this.setNewHighestBid(highestBid, bidHistory, incrementalBidAmount);
        return bidHistory;
    }

    protected BidHistory recordBidHistory(long auctionId, long userId, long amount) {
        BidHistory initBidHistory = BidHistory.builder()
                .auction(Auction.builder().id(auctionId).build())
                .user(User.builder().id(userId).build())
                .biddingAmount(amount)
                .biddingTime(this.biddingTime)
                .build();
        return bidHistoryStore.store(initBidHistory);
    }

    protected Long calcIncrementalBidAmount(long baseAmount) {
        int increment = IncrementUtil.getIncrement(baseAmount);
        return baseAmount + increment;
    }

    protected void setNewHighestBid(HighestBid highestBid, BidHistory latestBidHistory, long incrementalBidAmount) {
        highestBid.setUser(latestBidHistory.getUser());
        highestBid.setHighestBidAmount(latestBidHistory.getBiddingAmount());
        highestBid.setIncrementalBidAmount(incrementalBidAmount);
    }
}
