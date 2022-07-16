package com.mine.infrastructure.auction;

import com.mine.domain.auction.Auction;
import com.mine.domain.auction.AuctionStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AuctionStoreImpl implements AuctionStore {

    private final AuctionRepository auctionRepository;

    @Override
    public Auction store(Auction initAuction) {
        return auctionRepository.save(initAuction);
    }
}
