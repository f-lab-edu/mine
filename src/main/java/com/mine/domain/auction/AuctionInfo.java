package com.mine.domain.auction;

import lombok.Getter;
import lombok.Setter;

import java.net.URL;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
public class AuctionInfo {

    private final long id;
    private final String title;
    private final long startingPrice;
    private final ZonedDateTime closingTime;
    private final long highestBidAmount;
    private final long incrementalBidAmount;
    private final long userId;

    @Setter
    private List<URL> fileUrls = new ArrayList<>();

    public AuctionInfo(Auction auction) {
        this.id = auction.getId();
        this.title = auction.getTitle();
        this.startingPrice = auction.getStartingPrice();
        this.closingTime = auction.getClosingTime();
        this.highestBidAmount = auction.getHighestBid().getHighestBidAmount();
        this.incrementalBidAmount = auction.getHighestBid().getIncrementalBidAmount();
        this.userId = auction.getUser().getId();
    }
}
