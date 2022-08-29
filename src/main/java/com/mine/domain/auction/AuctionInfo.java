package com.mine.domain.auction;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class AuctionInfo {

    private final long id;
    private final String title;
    private final long startingPrice;
    private final ZonedDateTime closingTime;
    private final long userId;

    public AuctionInfo(Auction auction) {
        this.id = auction.getId();
        this.title = auction.getTitle();
        this.startingPrice = auction.getStartingPrice();
        this.closingTime = auction.getClosingTime();
        this.userId = auction.getUser().getId();
    }
}
