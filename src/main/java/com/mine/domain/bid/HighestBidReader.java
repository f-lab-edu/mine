package com.mine.domain.bid;

public interface HighestBidReader {

    HighestBid findByAuctionId(Long auctionId);
}
