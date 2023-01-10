package com.mine.domain.bid;

import com.mine.domain.user.User;
import org.springframework.stereotype.Service;

@Service("AutoBidRegisterService")
public class AutoBidRegisterServiceImpl extends AbstractBidRequest implements AutoBidRegisterService {

    private final AutoBidStore autoBidStore;
    private final AutoBidService autoBidService;

    public AutoBidRegisterServiceImpl(BidHistoryStore bidHistoryStore, AutoBidStore autoBidStore, AutoBidReader autoBidReader, AutoBidService autoBidService) {
        super(bidHistoryStore, autoBidReader);
        this.autoBidStore = autoBidStore;
        this.autoBidService = autoBidService;
    }

    @Override
    public BidInfo registerAutoBid(AutoBidRegisterCommand newAutoBid, AutoBidDto existingAutoBid, HighestBid highestBid) {
        super.validateBidAbleTime(highestBid.getAuction().getClosingTime());
        super.validateBidAbleAmount(newAutoBid.getLimit(), highestBid.getIncrementalBidAmount());

        if (existingAutoBid.getStorageType() == StorageType.CACHE || existingAutoBid.getStorageType() == StorageType.MAIN_DB) {
            if (newAutoBid.getLimit() <= existingAutoBid.getLimit()) {
                BidHistory newAutoBidHistory = super.bid(newAutoBid.getLimit(), newAutoBid.getAuctionId(), newAutoBid.getUserId(), highestBid);
                autoBidService.autoBid(existingAutoBid, highestBid);
                return new BidInfo(newAutoBidHistory);

            } else if (newAutoBid.getLimit() > existingAutoBid.getLimit()) {
                super.bid(existingAutoBid.getLimit(), existingAutoBid.getAuctionId(), existingAutoBid.getUserId(), highestBid);
                if (newAutoBid.getLimit() > highestBid.getIncrementalBidAmount()) {
                    BidHistory newAutoBidHistory = super.bid(highestBid.getIncrementalBidAmount(), newAutoBid.getAuctionId(), newAutoBid.getUserId(), highestBid);
                    registerNewAutoBid(newAutoBid, existingAutoBid);
                    return new BidInfo(newAutoBidHistory);
                } else if (newAutoBid.getLimit() <= highestBid.getIncrementalBidAmount()) {
                    BidHistory newAutoBidHistory = super.bid(newAutoBid.getLimit(), newAutoBid.getAuctionId(), newAutoBid.getUserId(), highestBid);
                    unregisterAutoBid(existingAutoBid);
                    return new BidInfo(newAutoBidHistory);
                }
            }

        } else if (existingAutoBid.getStorageType() == StorageType.NOT_STORED) {
            BidHistory newAutoBidHistory = super.bid(highestBid.getIncrementalBidAmount(), newAutoBid.getAuctionId(), newAutoBid.getUserId(), highestBid);
            if (newAutoBid.getLimit() > highestBid.getHighestBidAmount()) {
                registerNewAutoBid(newAutoBid);
            }
            return new BidInfo(newAutoBidHistory);
        }

        return null;
    }

    @Override
    public void registerNewAutoBid(AutoBidRegisterCommand newAutoBid) {
        AutoBid initAutoBid = newAutoBid.toAutoBidEntity();
        autoBidStore.store(initAutoBid);
    }

    @Override
    public void registerNewAutoBid(AutoBidRegisterCommand newAutoBid, AutoBidDto existingAutoBid) {
        if (existingAutoBid.getStorageType() == StorageType.CACHE) {
            updateAutoBidInCache(newAutoBid, existingAutoBid.getAutoBidCache());
            updateAutoBidInMainDB(newAutoBid);
        } else if (existingAutoBid.getStorageType() == StorageType.MAIN_DB) {
            updateAutoBidInMainDB(newAutoBid, existingAutoBid.getAutoBidMain());
            cacheAutoBid(newAutoBid);
        }
    }

    @Override
    public void cacheAutoBid(AutoBidRegisterCommand newAutoBid) {
        AutoBidCache initAutoBidCache = newAutoBid.toAutoBidCacheEntity();
        autoBidStore.storeInCache(initAutoBidCache);
    }

    @Override
    public void updateAutoBidInMainDB(AutoBidRegisterCommand newAutoBid) {
        autoBidStore.update(newAutoBid.getAuctionId(), newAutoBid.getUserId(), newAutoBid.getLimit());
    }

    @Override
    public void updateAutoBidInMainDB(AutoBidRegisterCommand newAutoBid, AutoBid autoBid) {
        autoBid.setUser(User.builder().id(newAutoBid.getUserId()).build());
        autoBid.setLimit(newAutoBid.getLimit());
        autoBidStore.store(autoBid);
    }

    @Override
    public void updateAutoBidInCache(AutoBidRegisterCommand newAutoBid, AutoBidCache autoBid) {
        autoBid.setUserId(newAutoBid.getUserId());
        autoBid.setLimit(newAutoBid.getLimit());
        autoBidStore.storeInCache(autoBid);
    }

    @Override
    public void unregisterAutoBid(AutoBidDto existingAutoBid) {
        if (existingAutoBid.getStorageType() == StorageType.CACHE) {
            autoBidStore.deleteByAuctionIdInCache(existingAutoBid.getAuctionId());
        }
        autoBidStore.deleteByAuctionId(existingAutoBid.getAuctionId());
    }
}
