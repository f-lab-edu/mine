package com.mine.application.bid;

import com.mine.domain.bid.AutoBidCommand;
import com.mine.domain.bid.BidCommand;
import com.mine.domain.bid.BidInfo;
import com.mine.domain.bid.BidService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BidFacade {

    private final BidService bidService;

    public BidInfo bid(BidCommand command) {
        return bidService.bid(command);
    }

    public BidInfo autoBid(AutoBidCommand command) {
        return bidService.autoBid(command);
    }
}
