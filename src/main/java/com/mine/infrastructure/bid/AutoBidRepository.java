package com.mine.infrastructure.bid;

import com.mine.domain.bid.AutoBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface AutoBidRepository extends JpaRepository<AutoBid, Long> {

    @Modifying
    @Query("UPDATE AutoBid ab SET ab.user.id = :userId, ab.limit = :limit WHERE ab.auction.id = :auctionId")
    void update(long auctionId, long userId, long limit);

    AutoBid findByAuctionId(long auctionId);
}
