package com.mine.infrastructure.auction;

import com.mine.domain.auction.Auction;
import com.mine.domain.auction.AuctionReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class AuctionReaderImpl implements AuctionReader {

    private final AuctionRepository auctionRepository;

    @Override
    public Auction read(Long auctionId) {
        Optional<Auction> auction = auctionRepository.findById(auctionId);
        return auction.orElseThrow();
    }
}
