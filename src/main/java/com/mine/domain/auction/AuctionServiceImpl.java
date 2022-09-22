package com.mine.domain.auction;

import com.mine.domain.bid.HighestBid;
import com.mine.domain.bid.HighestBidStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionStore auctionStore;
    private final AuctionReader auctionReader;
    private final HighestBidStore highestBidStore;

    @Override
    public AuctionInfo createAuction(AuctionCommand command) {
        // 경매 생성
        ZonedDateTime closingTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(command.getDuration());    // 경매 지속일을 더한 날짜/시간
        Auction initAuction = command.toEntity(closingTime);
        Auction auction = auctionStore.store(initAuction);

        // 입찰 가능한 최소 입찰가를 경매 시작가로 초기화
        HighestBid initHighestBid = command.toEntity(auction.getId());
        HighestBid highestBid = highestBidStore.store(initHighestBid);

        auction.setHighestBid(highestBid);
        return new AuctionInfo(auction);
    }

    @Override
    public AuctionInfo showCatalog(Long auctionId) {
        Auction auction = auctionReader.read(auctionId);
        return new AuctionInfo(auction);
    }
}
