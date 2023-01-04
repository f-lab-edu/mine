package com.mine.domain.bid;

import com.mine.common.exception.AuctionAlreadyClosedException;
import com.mine.common.exception.HighestBidAmountUpdateException;

import java.time.ZonedDateTime;

public abstract class AbstractBidRequest extends AbstractBid{

    protected final AutoBidReader autoBidReader;

    public AbstractBidRequest(BidHistoryStore bidHistoryStore, AutoBidReader autoBidReader) {
        super(bidHistoryStore);
        this.autoBidReader = autoBidReader;
    }

    void validateBidAbleTime(ZonedDateTime closingTime) {
        if(super.biddingTime.isEqual(closingTime) || super.biddingTime.isAfter(closingTime)) {
            throw new AuctionAlreadyClosedException();
        }
    }

    void validateBidAbleAmount(long amount, long incrementalBidAmount) {
        if(amount < incrementalBidAmount) {
            throw new HighestBidAmountUpdateException();
        }
    }

    public AutoBidDto findRegisteredAutoBid(long auctionId) {
        AutoBidDto autoBid = autoBidReader.findByAuctionIdInCache(auctionId);
        if (autoBid.getStorageType() != StorageType.CACHE) {
            autoBid = autoBidReader.findByAuctionId(auctionId);
        }
        return autoBid;
    }
}
