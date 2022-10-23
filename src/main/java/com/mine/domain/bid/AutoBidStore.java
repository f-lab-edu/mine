package com.mine.domain.bid;

public interface AutoBidStore {

    void store(AutoBid autoBid);

    void update(long auctionId, long userId, long limit);

    void storeInCache(AutoBidCache autoBidCache);

    void deleteByAuctionId(long auctionId);

    void deleteByAuctionIdInCache(long auctionId);
}
