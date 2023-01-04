package com.mine.domain.bid;

import org.springframework.stereotype.Service;

@Service("ManualBidService")
public class ManualBidServiceImpl extends AbstractBidRequest implements ManualBidService {

    public ManualBidServiceImpl(BidHistoryStore bidHistoryStore, AutoBidReader autoBidReader) {
        super(bidHistoryStore, autoBidReader);
    }

    @Override
    public BidInfo manualBid(BidCommand command, HighestBid highestBid) {
        super.validateBidAbleTime(highestBid.getAuction().getClosingTime());
        super.validateBidAbleAmount(command.getAmount(), highestBid.getIncrementalBidAmount());
        BidHistory bidHistory = super.bid(command.getAmount(), command.getAuctionId(), command.getUserId(), highestBid);
        return new BidInfo(bidHistory);
    }
}
