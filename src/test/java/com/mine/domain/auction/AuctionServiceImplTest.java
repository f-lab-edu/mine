package com.mine.domain.auction;

import com.mine.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.ZoneId;
import java.time.ZonedDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuctionServiceImplTest {

    @Mock
    AuctionStore auctionStore;

    @InjectMocks
    AuctionServiceImpl auctionService;

    @Test
    @DisplayName("경매 생성")
    void createAuction() {
        AuctionCommand command = AuctionCommand.builder()
                .userId(1L)
                .title("New Auction")
                .startingPrice(50000L)
                .duration(7)
                .build();

        Auction savedAuction = Auction.builder()
                .id(1L)
                .user(User.builder().id(1L).build())
                .title("New Auction")
                .startingPrice(50000L)
                .closingTime(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(command.getDuration()))
                .build();

        when(auctionStore.store(any())).thenReturn(savedAuction);

        AuctionInfo auctionInfo = auctionService.createAuction(command);

        assertEquals(savedAuction.getId(), auctionInfo.getId());
    }

    @DisplayName("경매 카탈로그 조회")
    void showCatalog() {
        Auction readAuction = Auction.builder()
                .id(1L)
                .user(User.builder().id(1L).build())
                .title("New Auction")
                .startingPrice(50000L)
                .closingTime(ZonedDateTime.parse("2022-08-12 12:37:09"))
                .build();

        AuctionInfo auctionInfo = auctionService.showCatalog(1L);

        assertEquals(1L, auctionInfo.getId());
    }
}
