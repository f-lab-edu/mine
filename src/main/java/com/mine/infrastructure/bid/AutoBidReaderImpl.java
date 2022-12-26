package com.mine.infrastructure.bid;

import com.mine.domain.bid.AutoBid;
import com.mine.domain.bid.AutoBidCache;
import com.mine.domain.bid.AutoBidReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AutoBidReaderImpl implements AutoBidReader {

    private final AutoBidRepository autoBidRepository;
    private final AutoBidCacheRepository autoBidCacheRepository;

    @Override
    public AutoBidDto findByAuctionId(long auctionId) {
        AutoBid autoBid = autoBidRepository.findByAuctionId(auctionId);
        if (autoBid != null) {
            return AutoBidDto.builder()
                    .auctionId(autoBid.getAuction().getId())
                    .userId(autoBid.getUser().getId())
                    .limit(autoBid.getLimit())
                    .storageType(StorageType.Main)
                    .autoBidMain(autoBid)
                    .build();
        } else {
            return null;
        }
    }

    @Override
    public AutoBidDto findByAuctionIdInCache(long auctionId) {
        Optional<AutoBidCache> autoBidCache = autoBidCacheRepository.findById(auctionId);
        if (autoBidCache.isPresent()) {
            return AutoBidDto.builder()
                    .auctionId(autoBidCache.get().getAuctionId())
                    .userId(autoBidCache.get().getUserId())
                    .limit(autoBidCache.get().getLimit())
                    .storageType(StorageType.Cache)
                    .autoBidCache(autoBidCache.get())
                    .build();
        } else {
            return null;
        }
    }
}
