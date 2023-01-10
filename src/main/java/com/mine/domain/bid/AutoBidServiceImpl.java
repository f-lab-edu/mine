package com.mine.domain.bid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AutoBidServiceImpl extends AbstractBid implements AutoBidService {

    private final AutoBidStore autoBidStore;

    @Autowired
    public AutoBidServiceImpl(BidHistoryStore bidHistoryStore, AutoBidStore autoBidStore) {
        super(bidHistoryStore);
        this.autoBidStore = autoBidStore;
    }

    @Override
    public void autoBid(AutoBidDto autoBid, HighestBid highestBid) {
        if (autoBid.getLimit() > highestBid.getIncrementalBidAmount()) {
            super.bid(highestBid.getIncrementalBidAmount(), autoBid.getAuctionId(), autoBid.getUserId(), highestBid);
            if (autoBid.getStorageType() == StorageType.MAIN_DB) {
                cacheAutoBid(autoBid);
            }
        } else if (autoBid.getLimit() == highestBid.getIncrementalBidAmount()) {
            super.bid(autoBid.getLimit(), autoBid.getAuctionId(), autoBid.getUserId(), highestBid);
            unregisterAutoBid(autoBid);
        } else if (autoBid.getLimit() < highestBid.getIncrementalBidAmount()) {
            bidForExceptionalCase(autoBid, highestBid);
            unregisterAutoBid(autoBid);
        }
    }

    @Override
    public void bidForExceptionalCase(AutoBidDto autoBid, HighestBid highestBid) {
        BidHistory bidHistory = super.recordBidHistory(autoBid.getAuctionId(), autoBid.getUserId(), autoBid.getLimit());
        Long incrementalBidAmount = super.calcIncrementalBidAmount(bidHistory.getBiddingAmount());
        if (bidHistory.getBiddingAmount() >= highestBid.getHighestBidAmount()) {
            super.setNewHighestBid(highestBid, bidHistory, incrementalBidAmount);
        }
    }

    @Override
    public void cacheAutoBid(AutoBidDto autoBid) {
        AutoBidCache initAutoBidCache = autoBid.toAutoBidCacheEntity();
        autoBidStore.storeInCache(initAutoBidCache);
    }

    @Override
    public void unregisterAutoBid(AutoBidDto autoBid) {
        if (autoBid.getStorageType() == StorageType.CACHE) {
            autoBidStore.deleteByAuctionIdInCache(autoBid.getAuctionId());
        }
        autoBidStore.deleteByAuctionId(autoBid.getAuctionId());
    }
}
