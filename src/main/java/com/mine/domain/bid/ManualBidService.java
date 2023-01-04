package com.mine.domain.bid;

public interface ManualBidService {

    BidInfo manualBid(BidCommand command, HighestBid highestBid);

}
