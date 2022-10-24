package com.mine.infrastructure.bid;

import com.mine.domain.bid.AutoBid;
import com.mine.domain.bid.AutoBidCache;
import com.mine.domain.bid.AutoBidStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AutoBidStoreImpl implements AutoBidStore {

    private final AutoBidRepository autoBidRepository;
    private final AutoBidCacheRepository autoBidCacheRepository;

    @Override
    public void store(AutoBid autoBid) {
        autoBidRepository.save(autoBid);
    }

    @Override
    public void update(long auctionId, long userId, long limit) {
        autoBidRepository.update(auctionId, userId, limit);
    }

    @Override
    public void storeInCache(AutoBidCache autoBidCache) {
        autoBidCacheRepository.save(autoBidCache);
    }

    @Override
    public void deleteByAuctionId(long auctionId) {
        autoBidRepository.deleteByAuctionId(auctionId);
    }

    @Override
    public void deleteByAuctionIdInCache(long auctionId) {
        autoBidCacheRepository.deleteById(auctionId);
    }

}
