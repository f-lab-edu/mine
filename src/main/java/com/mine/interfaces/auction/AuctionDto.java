package com.mine.interfaces.auction;

import com.mine.domain.auction.AuctionCommand;
import com.mine.domain.auction.AuctionInfo;
import com.mine.util.SecurityUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class AuctionDto {

    @Setter
    public static class CreateAuctionRequest {

        @NotBlank
        private String title;

        @NotNull
        @PositiveOrZero
        private long startingPrice;

        @NotNull
        private int duration;

        public AuctionCommand toCommand(MultipartFile[] images) {
            return AuctionCommand.builder()
                    .userId(SecurityUtil.getCurrentMemberId())
                    .title(this.title)
                    .images(images)
                    .startingPrice(this.startingPrice)
                    .duration(this.duration)
                    .build();
        }
    }

    @Getter
    public static class CreateAuctionResponse {

        private final long id;
        private final String title;
        private final long startingPrice;
        private final String closingTime;
        private final ZonedDateTime utc;
        private final long highestPrice;
        private final long atLeast;
        private final long userId;

        public CreateAuctionResponse(AuctionInfo auctionInfo) {
            this.id = auctionInfo.getId();
            this.title = auctionInfo.getTitle();
            this.startingPrice = auctionInfo.getStartingPrice();
            this.closingTime = getLocalClosingTime(auctionInfo.getClosingTime());
            this.utc = auctionInfo.getClosingTime();
            this.highestPrice = auctionInfo.getHighestPrice();
            this.atLeast = auctionInfo.getAtLeast();
            this.userId = auctionInfo.getUserId();
        }

        private String getLocalClosingTime(ZonedDateTime utc) {
            LocalDateTime time = utc.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();    // UTC 협정 세계시를 ZoneId 지역의 날짜/시간으로 변경
            return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT).format(time);
        }
    }

    @Getter
    public static class CatalogResponse {

        private final Long id;
        private final String title;
        private final Long startingPrice;
        private final String closingTime;
        private final long highestPrice;
        private final long atLeast;
        private final Long userId;
        private final List<URL> fileUrls;

        public CatalogResponse(AuctionInfo auctionInfo) {
            this.id = auctionInfo.getId();
            this.title = auctionInfo.getTitle();
            this.startingPrice = auctionInfo.getStartingPrice();
            this.closingTime = getLocalClosingTime(auctionInfo.getClosingTime());
            this.highestPrice = auctionInfo.getHighestPrice();
            this.atLeast = auctionInfo.getAtLeast();
            this.userId = auctionInfo.getUserId();
            this.fileUrls = new ArrayList<>(auctionInfo.getFileUrls());
        }

        private String getLocalClosingTime(ZonedDateTime utc) {
            LocalDateTime time = utc.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();    // UTC 협정 세계시를 ZoneId 지역의 날짜/시간으로 변경
            return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT).format(time);
        }
    }
}
