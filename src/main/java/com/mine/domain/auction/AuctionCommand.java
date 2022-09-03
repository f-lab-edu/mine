package com.mine.domain.auction;

import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;

@Builder
public class AuctionCommand {

    private final long userId;

    private final String title;

    @Getter
    private final MultipartFile[] images;

    private final long startingPrice;

    @Getter
    private final int duration;

    public Auction toEntity(ZonedDateTime closingTime) {
        return Auction.builder()
                .user(User.builder().id(this.userId).build())
                .title(this.title)
                .startingPrice(this.startingPrice)
                .closingTime(closingTime)
                .build();
    }
}
