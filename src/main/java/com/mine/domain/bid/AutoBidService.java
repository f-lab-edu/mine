package com.mine.domain.bid;

public interface AutoBidService {

    void autoBid(AutoBidDto autoBid, HighestBid highestBid);

    void bidForExceptionalCase(AutoBidDto autoBid, HighestBid highestBid);

    void cacheAutoBid(AutoBidDto autoBid);

    void unregisterAutoBid(AutoBidDto autoBid);
}
