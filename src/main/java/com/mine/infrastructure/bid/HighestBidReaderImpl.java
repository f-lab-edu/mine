package com.mine.infrastructure.bid;

import com.mine.domain.bid.HighestBid;
import com.mine.domain.bid.HighestBidReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HighestBidReaderImpl implements HighestBidReader {

    private final HighestBidRepository highestBidRepository;

    @Override
    public HighestBid findByAuctionId(Long auctionId) {
        return highestBidRepository.findByAuctionId(auctionId);
    }
}