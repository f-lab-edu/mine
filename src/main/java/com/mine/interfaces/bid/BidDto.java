package com.mine.interfaces.bid;

import com.mine.domain.bid.AutoBidCommand;
import com.mine.domain.bid.BidCommand;
import com.mine.domain.bid.BidInfo;
import com.mine.util.SecurityUtil;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class BidDto {

    @Setter
    public static class BidRequest {

        private long price;

        public BidCommand toCommand(long auctionId) {
            return BidCommand.builder()
                    .auctionId(auctionId)
                    .userId(SecurityUtil.getCurrentMemberId())
                    .price(this.price)
                    .build();
        }
    }

    @Setter
    public static class AutoBidRequest {

        private long limit;

        public AutoBidCommand toCommand(long auctionId) {
            return AutoBidCommand.builder()
                    .auctionId(auctionId)
                    .userId(SecurityUtil.getCurrentMemberId())
                    .limit(this.limit)
                    .build();
        }
    }

    @Getter
    public static class BidResponse {

        private final long bidHistoryId;
        private final long auctionId;
        private final long userId;
        private final long biddingPrice;
        private final String biddingTime;

        public BidResponse(BidInfo bidInfo) {
            this.bidHistoryId = bidInfo.getBidHistoryId();
            this.auctionId = bidInfo.getAuctionId();
            this.userId = bidInfo.getUserId();
            this.biddingPrice = bidInfo.getBiddingPrice();
            this.biddingTime = getLocalBiddingTime(bidInfo.getBiddingTime());
        }

        private String getLocalBiddingTime(ZonedDateTime utc) {
            LocalDateTime time = utc.withZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDateTime();
            return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL, FormatStyle.SHORT).format(time);
        }
    }
}
