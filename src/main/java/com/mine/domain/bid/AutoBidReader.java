package com.mine.domain.bid;

import com.mine.infrastructure.bid.AutoBidDto;

public interface AutoBidReader {

    AutoBidDto findByAuctionId(long auctionId);

    AutoBidDto findByAuctionIdInCache(long auctionId);
}
