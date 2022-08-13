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
                .user(new User(1L))
                .title("New Auction")
                .startingPrice(50000L)
                .closingTime(ZonedDateTime.now(ZoneId.of("UTC")).plusDays(command.getDuration()))
                .build();

        when(auctionStore.store(any())).thenReturn(savedAuction);

        AuctionInfo auctionInfo = auctionService.createAuction(command);

        assertEquals(1L, auctionInfo.getUserId());
    }
}
