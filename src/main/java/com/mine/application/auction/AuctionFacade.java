package com.mine.application.auction;

import com.mine.domain.auction.AuctionCommand;
import com.mine.domain.auction.AuctionInfo;
import com.mine.domain.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuctionFacade {

    private final AuctionService auctionService;

    public AuctionInfo createAuction(AuctionCommand command) {
        return auctionService.createAuction(command);
    }
}