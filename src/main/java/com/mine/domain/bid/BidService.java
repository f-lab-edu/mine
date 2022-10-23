package com.mine.domain.bid;

public interface BidService {

    BidInfo bid(BidCommand command);

    BidInfo autoBid(AutoBidCommand command);
}
