package com.mine.domain.bid;

public interface AutoBidRegisterService {

    BidInfo registerAutoBid(AutoBidRegisterCommand newAutoBid, AutoBidDto existingAutoBid, HighestBid highestBid);

    void registerNewAutoBid(AutoBidRegisterCommand newAutoBid);

    void registerNewAutoBid(AutoBidRegisterCommand newAutoBid, AutoBidDto existingAutoBid);

    void cacheAutoBid(AutoBidRegisterCommand newAutoBid);

    void updateAutoBidInMainDB(AutoBidRegisterCommand newAutoBid);

    void updateAutoBidInMainDB(AutoBidRegisterCommand newAutoBid, AutoBid autoBid);

    void updateAutoBidInCache(AutoBidRegisterCommand newAutoBid, AutoBidCache autoBid);

    void unregisterAutoBid(AutoBidDto existingAutoBid);
}
