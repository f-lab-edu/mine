package com.mine.domain.auction;

import lombok.Getter;

import java.time.ZonedDateTime;

@Getter
public class AuctionInfo {

    private final Long id;
    private final String title;
    private final Long startingPrice;
    private final ZonedDateTime closingTime;
    private final Long userId;

    public AuctionInfo(Auction auction) {
        this.id = auction.getId();
        this.title = auction.getTitle();
        this.startingPrice = auction.getStartingPrice();
        this.closingTime = auction.getClosingTime();
        this.userId = auction.getUser().getId();
    }
}
