package com.mine.domain.auction;

public interface AuctionService {

    AuctionInfo createAuction(AuctionCommand command);

    AuctionInfo showCatalog(Long auctionId);
}