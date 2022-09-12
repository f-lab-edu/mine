package com.mine.infrastructure.bid;

import com.mine.domain.bid.HighestBid;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.LockModeType;

public interface HighestBidRepository extends JpaRepository<HighestBid, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    HighestBid findByAuctionId(Long auctionId);
}
