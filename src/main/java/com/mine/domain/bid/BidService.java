package com.mine.domain.bid;

public interface BidService {

    BidInfo manualBid(BidCommand command);

    BidInfo autoBid(AutoBidCommand command);
}
