package com.mine.domain.bid;

import com.mine.common.exception.AuctionAlreadyClosedException;
import com.mine.common.exception.HighestBidPriceUpdateException;
import com.mine.domain.auction.Auction;
import com.mine.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BidServiceImplTest {

    @Mock
    HighestBidReader highestBidReader;

    @Mock
    HighestBidStore highestBidStore;

    @Mock
    BidHistoryStore bidHistoryStore;

    @InjectMocks
    BidServiceImpl bidService;

    BidCommand command;

    @BeforeEach
    void setUp() {
        command = BidCommand.builder()
                .auctionId(1)
                .userId(1)
                .price(100000)
                .build();
    }

    @Test
    @DisplayName("입찰 성공")
    void successBid() {
        HighestBid currentHighestBid = HighestBid.builder()
                .auction(Auction.builder().closingTime(ZonedDateTime.parse("2100-10-25T08:28:53.444790Z[UTC]")).build())
                .highestPrice(50000)
                .atLeast(52000)
                .build();

        BidHistory savedOneHistory = BidHistory.builder()
                .id(1L)
                .auction(Auction.builder().id(1L).build())
                .user(User.builder().id(1L).build())
                .biddingPrice(100000)
                .biddingTime(ZonedDateTime.now(ZoneId.of("UTC")))
                .build();

        when(highestBidReader.findByAuctionId(command.getAuctionId())).thenReturn(currentHighestBid);
        when(bidHistoryStore.store(any())).thenReturn(savedOneHistory);

        BidInfo bidInfo = bidService.bid(command);

        assertEquals(command.getUserId(), bidInfo.getUserId());
        assertEquals(command.getPrice(), bidInfo.getBiddingPrice());
    }

    @Test
    @DisplayName("마감된 경매에 입찰한 경우 입찰 실패")
    void throwWhenClosedAuction() {
        HighestBid currentHighestBid = HighestBid.builder()
                .auction(Auction.builder().closingTime(ZonedDateTime.parse("2022-08-20T15:28:53.444790Z[UTC]")).build())
                .highestPrice(50000)
                .atLeast(52000)
                .build();

        when(highestBidReader.findByAuctionId(command.getAuctionId())).thenReturn(currentHighestBid);

        AuctionAlreadyClosedException e = assertThrows(AuctionAlreadyClosedException.class, () -> bidService.bid(command));

        assertEquals("이미 마감된 경매입니다. 더 이상 입찰할 수 없습니다.", e.getMessage());
    }

    @Test
    @DisplayName("입찰 가능한 최소 입찰가 미만으로 입찰한 경우 입찰 실패")
    void throwWhenLowBiddingPrice() {
        HighestBid currentHighestBid = HighestBid.builder()
                .auction(Auction.builder().closingTime(ZonedDateTime.parse("2100-10-25T08:28:53.444790Z[UTC]")).build())
                .highestPrice(500000)
                .atLeast(507500)
                .build();

        when(highestBidReader.findByAuctionId(command.getAuctionId())).thenReturn(currentHighestBid);

        HighestBidPriceUpdateException e = assertThrows(HighestBidPriceUpdateException.class, () -> bidService.bid(command));

        assertEquals("입찰에 실패했습니다. 입찰 가능한 최소 입찰가 이상으로 다시 입찰해 주세요.", e.getMessage());
    }
}
