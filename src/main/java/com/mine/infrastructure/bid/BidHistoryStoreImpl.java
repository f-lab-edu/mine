package com.mine.infrastructure.bid;

import com.mine.domain.bid.BidHistory;
import com.mine.domain.bid.BidHistoryStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BidHistoryStoreImpl implements BidHistoryStore {

    private final BidHistoryRepository bidHistoryRepository;

    @Override
    public BidHistory store(BidHistory initBidHistory) {
        return bidHistoryRepository.save(initBidHistory);
    }
}