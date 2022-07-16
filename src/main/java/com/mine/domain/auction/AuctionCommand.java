package com.mine.domain.auction;

import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.web.multipart.MultipartFile;

import java.time.ZonedDateTime;

@Builder
public class AuctionCommand {

    @Getter
    private final Long userId;

    private final String title;

    @Getter
    private final MultipartFile[] images;

    private final Long startingPrice;

    @Getter
    private final int duration;

    public Auction toEntity(ZonedDateTime closingTime) {
        return Auction.builder()
                .user(new User(userId))
                .title(this.title)
                .startingPrice(this.startingPrice)
                .closingTime(closingTime)
                .build();
    }
}
