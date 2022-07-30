package com.mine.domain.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionStore auctionStore;
    private final AuctionReader auctionReader;

    @Override
    public AuctionInfo createAuction(AuctionCommand command) {
        ZonedDateTime closingTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(command.getDuration());    // 경매 지속일을 더한 날짜/시간
        Auction initAuction = command.toEntity(closingTime);
        Auction auction = auctionStore.store(initAuction);
        return new AuctionInfo(auction);
    }

    @Override
    public AuctionInfo showCatalog(Long auctionId) {
        Auction auction = auctionReader.read(auctionId);
        return new AuctionInfo(auction);
    }
}
