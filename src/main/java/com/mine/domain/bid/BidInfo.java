package com.mine.domain.bid;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class BidInfo {

    private final long bidHistoryId;
    private final long auctionId;
    private final long userId;
    private final long biddingPrice;
    private final ZonedDateTime biddingTime;

    public BidInfo(BidHistory bidHistory) {
        this.bidHistoryId = bidHistory.getId();
        this.auctionId = bidHistory.getAuction().getId();
        this.userId = bidHistory.getUser().getId();
        this.biddingPrice = bidHistory.getBiddingAmount();
        this.biddingTime = bidHistory.getBiddingTime();
    }
}
