package com.mine.infrastructure.bid;

import com.mine.domain.bid.HighestBid;
import com.mine.domain.bid.HighestBidStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HighestBidStoreImpl implements HighestBidStore {

    private final HighestBidRepository highestBidRepository;

    @Override
    public HighestBid store(HighestBid newHighestBid) {
        return highestBidRepository.save(newHighestBid);
    }
}
