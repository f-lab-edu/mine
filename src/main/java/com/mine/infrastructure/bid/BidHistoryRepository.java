package com.mine.infrastructure.bid;

import com.mine.domain.bid.BidHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BidHistoryRepository extends JpaRepository<BidHistory, Long> {

}
