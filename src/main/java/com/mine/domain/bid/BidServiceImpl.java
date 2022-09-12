package com.mine.domain.bid;

import com.mine.common.exception.AuctionAlreadyClosedException;
import com.mine.common.exception.HighestBidPriceUpdateException;
import com.mine.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final HighestBidReader highestBidReader;
    private final HighestBidStore highestBidStore;
    private final BidHistoryStore bidHistoryStore;

    @Override
    @Transactional
    public BidInfo bid(BidCommand command) {
        HighestBid currentHighestBid = highestBidReader.findByAuctionId(command.getAuctionId());
        ZonedDateTime closingTime = currentHighestBid.getAuction().getClosingTime();
        ZonedDateTime biddingTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // 마감된 경매일 경우 입찰 실패
        if(biddingTime.isEqual(closingTime) || biddingTime.isAfter(closingTime)) {
            throw new AuctionAlreadyClosedException();
        }

        // 최고 입찰가보다 낮은 입찰가로 입찰할 경우 입찰 실패
        if(command.getPrice() <= currentHighestBid.getHighestPrice()) {
            throw new HighestBidPriceUpdateException();
        }

        // 최고 입찰가 갱신
        currentHighestBid.setUser(User.builder().id(command.getUserId()).build());
        currentHighestBid.setHighestPrice(command.getPrice());
        highestBidStore.store(currentHighestBid);

        // 입찰 내역 반영
        BidHistory initBidHistory = command.toEntity(biddingTime);
        BidHistory oneHistory = bidHistoryStore.store(initBidHistory);

        // 반영된 내역 반환
        return new BidInfo(oneHistory);
    }
}
