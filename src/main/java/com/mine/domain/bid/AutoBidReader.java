package com.mine.domain.bid;

public interface AutoBidReader {

    AutoBidDto findByAuctionId(long auctionId);

    AutoBidDto findByAuctionIdInCache(long auctionId);
}
