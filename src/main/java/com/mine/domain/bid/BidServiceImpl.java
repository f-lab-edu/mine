package com.mine.domain.bid;

import com.mine.common.exception.AuctionAlreadyClosedException;
import com.mine.common.exception.HighestBidPriceUpdateException;
import com.mine.domain.user.User;
import com.mine.infrastructure.bid.AutoBidDto;
import com.mine.infrastructure.bid.StorageType;
import com.mine.util.IncrementUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class BidServiceImpl implements BidService {

    private final HighestBidReader highestBidReader;
    private final HighestBidStore highestBidStore;
    private final BidHistoryStore bidHistoryStore;
    private final AutoBidReader autoBidReader;
    private final AutoBidStore autoBidStore;

    @Override
    @Transactional
    public BidInfo manualBid(BidCommand manualBid) {
        HighestBid currentHighestBid = highestBidReader.findByAuctionId(manualBid.getAuctionId());
        ZonedDateTime closingTime = currentHighestBid.getAuction().getClosingTime();
        ZonedDateTime biddingTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // 마감된 경매일 경우 입찰 실패
        if(biddingTime.isEqual(closingTime) || biddingTime.isAfter(closingTime)) {
            throw new AuctionAlreadyClosedException();
        }

        // 입찰 금액이 입찰 가능 최소 금액보다 적을 경우 입찰 실패
        if(manualBid.getPrice() < currentHighestBid.getAtLeast()) {
            throw new HighestBidPriceUpdateException();
        }

        // 수동 입찰 수행
        BidHistory manualBidHistory = bidManualUsingRequestedAmount(manualBid, biddingTime);
        long nextMinimumBidAmount = updateHighestBidToManualBid(manualBid, currentHighestBid);

        // 현 경매에 등록된 자동 입찰 확인
        AutoBidDto existingAutoBid = findAutoBid(manualBid.getAuctionId());

        // 자동 입찰 수행
        if (existingAutoBid != null) {
            executeExistingAutoBid(existingAutoBid, nextMinimumBidAmount, biddingTime, currentHighestBid);
        }

        return new BidInfo(manualBidHistory);
    }

    @Override
    @Transactional
    public BidInfo autoBid(AutoBidCommand newAutoBid) {
        HighestBid currentHighestBid = highestBidReader.findByAuctionId(newAutoBid.getAuctionId());
        ZonedDateTime closingTime = currentHighestBid.getAuction().getClosingTime();
        ZonedDateTime biddingTime = ZonedDateTime.now(ZoneId.of("UTC"));

        // 마감된 경매일 경우 입찰 실패
        if (biddingTime.isEqual(closingTime) || biddingTime.isAfter(closingTime)) {
            throw new AuctionAlreadyClosedException();
        }

        // 신규 자동 입찰 한도가 입찰 가능 최소 금액보다 적을 경우 자동 입찰 등록 실패
        if (newAutoBid.getLimit() < currentHighestBid.getAtLeast()) {
            throw new HighestBidPriceUpdateException();
        }

        // 현 경매에 등록된 자동 입찰 확인
        AutoBidDto existingAutoBid = findAutoBid(newAutoBid.getAuctionId());

        if (existingAutoBid == null) {
            // 신규 자동 입찰 즉시 등록
            BidHistory myBidHistory = register(newAutoBid, biddingTime, currentHighestBid);
            return new BidInfo(myBidHistory);
        } else {
            // 신규 자동 입찰과 기존 자동 입찰 경쟁
            BidHistory myBidHistory = competeBetween(newAutoBid, existingAutoBid, biddingTime, currentHighestBid);
            return new BidInfo(myBidHistory);
        }
    }

    public AutoBidDto findAutoBid(long auctionId) {
        AutoBidDto autoBidDto = autoBidReader.findByAuctionIdInCache(auctionId);
        if (autoBidDto == null) {
            autoBidDto = autoBidReader.findByAuctionId(auctionId);
        }
        return autoBidDto;
    }

    public void executeExistingAutoBid(AutoBidDto existingAutoBid, long minimumBidAmount, ZonedDateTime biddingTime, HighestBid currentHighestBid) {
        if (existingAutoBid.getLimit() > minimumBidAmount) {
            bidExistingUsingMinimumAmount(existingAutoBid, minimumBidAmount, biddingTime);
            long nextMinimumBidAmount = updateHighestBidToExistingAutoBid(existingAutoBid, minimumBidAmount, currentHighestBid);

            if (existingAutoBid.getLimit() >= nextMinimumBidAmount) {
                registerExistingAutoBidInCache(existingAutoBid);
            } else if (existingAutoBid.getLimit() < nextMinimumBidAmount) {
                deleteExistingAutoBid(existingAutoBid);
            }

        } else if (existingAutoBid.getLimit() <= minimumBidAmount) {
            bidExistingUsingLimit(existingAutoBid, biddingTime);
            updateHighestBidToExistingAutoBid(existingAutoBid, existingAutoBid.getLimit(), currentHighestBid);
            deleteExistingAutoBid(existingAutoBid);
        }
    }

    public BidHistory register(AutoBidCommand newAutoBid, ZonedDateTime biddingTime, HighestBid currentHighestBid) {
        BidHistory newAutoBidHistory = bidNewUsingMinimumAmount(newAutoBid, currentHighestBid.getAtLeast(), biddingTime);
        long nextMinimumBidAmount = updateHighestBidToNewAutoBid(newAutoBid, newAutoBidHistory.getBiddingPrice(), currentHighestBid);

        if (newAutoBid.getLimit() >= nextMinimumBidAmount) {
            AutoBid initNewAutoBid = newAutoBid.toEntity();
            autoBidStore.store(initNewAutoBid);
        }

        return newAutoBidHistory;
    }

    public BidHistory competeBetween(AutoBidCommand newAutoBid, AutoBidDto existingAutoBid, ZonedDateTime biddingTime, HighestBid currentHighestBid) {
        if (newAutoBid.getLimit() > existingAutoBid.getLimit()) {
            return winNewBecauseBiggerLimit(newAutoBid, existingAutoBid, biddingTime, currentHighestBid);
        } else if (newAutoBid.getLimit() < existingAutoBid.getLimit()) {
            return winExistingBecauseBiggerLimit(newAutoBid, existingAutoBid, biddingTime, currentHighestBid);
        } else if (newAutoBid.getLimit() == existingAutoBid.getLimit()) {
            return winExistingBecauseSameLimit(newAutoBid, existingAutoBid, biddingTime, currentHighestBid);
        }
        return null;
    }

    public BidHistory winNewBecauseBiggerLimit(AutoBidCommand newAutoBid, AutoBidDto existingAutoBid, ZonedDateTime biddingTime, HighestBid currentHighestBid) {
        bidExistingUsingLimit(existingAutoBid, biddingTime);
        long nextMinimumBidAmount = calculateNextMinimumBidAmount(existingAutoBid.getLimit());

        if (newAutoBid.getLimit() > nextMinimumBidAmount) {
            BidHistory newAutoBidHistory = bidNewUsingMinimumAmount(newAutoBid, nextMinimumBidAmount, biddingTime);
            nextMinimumBidAmount = updateHighestBidToNewAutoBid(newAutoBid, nextMinimumBidAmount, currentHighestBid);

            if (newAutoBid.getLimit() >= nextMinimumBidAmount) {
                registerNewAutoBid(newAutoBid, existingAutoBid);
            } else if (newAutoBid.getLimit() < nextMinimumBidAmount) {
                deleteExistingAutoBid(existingAutoBid);
            }

            return newAutoBidHistory;

        } else if (newAutoBid.getLimit() <= nextMinimumBidAmount) {
            BidHistory newAutoBidHistory = bidNewUsingLimit(newAutoBid, biddingTime);
            updateHighestBidToNewAutoBid(newAutoBid, newAutoBid.getLimit(), currentHighestBid);
            deleteExistingAutoBid(existingAutoBid);

            return newAutoBidHistory;
        }

        return null;
    }

    public BidHistory winExistingBecauseBiggerLimit(AutoBidCommand newAutoBid, AutoBidDto existingAutoBid, ZonedDateTime biddingTime, HighestBid currentHighestBid) {
        BidHistory newAutoBidHistory = bidNewUsingLimit(newAutoBid, biddingTime);
        long nextMinimumBidAmount = calculateNextMinimumBidAmount(newAutoBid.getLimit());

        if (existingAutoBid.getLimit() > nextMinimumBidAmount) {
            bidExistingUsingMinimumAmount(existingAutoBid, nextMinimumBidAmount, biddingTime);
            nextMinimumBidAmount = updateHighestBidToExistingAutoBid(existingAutoBid, nextMinimumBidAmount, currentHighestBid);

            if (existingAutoBid.getLimit() >= nextMinimumBidAmount) {
                registerExistingAutoBidInCache(existingAutoBid);
            } else if (existingAutoBid.getLimit() < nextMinimumBidAmount) {
                deleteExistingAutoBid(existingAutoBid);
            }

        } else if (existingAutoBid.getLimit() <= nextMinimumBidAmount) {
            bidExistingUsingLimit(existingAutoBid, biddingTime);
            updateHighestBidToExistingAutoBid(existingAutoBid, existingAutoBid.getLimit(), currentHighestBid);
            deleteExistingAutoBid(existingAutoBid);
        }

        return newAutoBidHistory;
    }

    public BidHistory winExistingBecauseSameLimit(AutoBidCommand newAutoBid, AutoBidDto existingAutoBid, ZonedDateTime biddingTime, HighestBid currentHighestBid) {
        BidHistory newAutoBidHistory = bidNewUsingLimit(newAutoBid, biddingTime);
        bidExistingUsingLimit(existingAutoBid, biddingTime);
        updateHighestBidToExistingAutoBid(existingAutoBid, existingAutoBid.getLimit(), currentHighestBid);
        deleteExistingAutoBid(existingAutoBid);
        return newAutoBidHistory;
    }

    public BidHistory bidManualUsingRequestedAmount(BidCommand manualBid, ZonedDateTime biddingTime) {
        BidHistory initManualBidHistory = manualBid.toHistoryEntity(biddingTime);
        return bidHistoryStore.store(initManualBidHistory);
    }

    public BidHistory bidNewUsingMinimumAmount(AutoBidCommand newAutoBid, long minimumAmount, ZonedDateTime biddingTime) {
        BidHistory initNewAutoBidHistory = newAutoBid.toBidHistoryEntity(minimumAmount, biddingTime);
        return bidHistoryStore.store(initNewAutoBidHistory);
    }

    public BidHistory bidNewUsingLimit(AutoBidCommand newAutoBid, ZonedDateTime biddingTime) {
        BidHistory initNewAutoBidHistory = newAutoBid.toBidHistoryEntity(biddingTime);
        return bidHistoryStore.store(initNewAutoBidHistory);
    }

    public BidHistory bidExistingUsingMinimumAmount(AutoBidDto existingAutoBid, long minimumAmount, ZonedDateTime biddingTime) {
        BidHistory initExistingBidHistory = existingAutoBid.toBidHistoryEntity(minimumAmount, biddingTime);
        return bidHistoryStore.store(initExistingBidHistory);
    }

    public BidHistory bidExistingUsingLimit(AutoBidDto existingAutoBid, ZonedDateTime biddingTime) {
        BidHistory initExistingAutoBid = existingAutoBid.toBidHistoryEntity(biddingTime);
        return bidHistoryStore.store(initExistingAutoBid);
    }

    public long updateHighestBidToManualBid(BidCommand manualBid, HighestBid currentHighestBid) {
        long nextMinimumBidAmount = calculateNextMinimumBidAmount(manualBid.getPrice());
        currentHighestBid.setHighestPrice(manualBid.getPrice());
        currentHighestBid.setAtLeast(nextMinimumBidAmount);
        currentHighestBid.setUser(User.builder().id(manualBid.getUserId()).build());
        highestBidStore.store(currentHighestBid);
        return nextMinimumBidAmount;
    }

    public long updateHighestBidToNewAutoBid(AutoBidCommand newAutoBid, long biddingAmount, HighestBid currentHighestBid) {
        long nextMinimumBidAmount = calculateNextMinimumBidAmount(biddingAmount);
        currentHighestBid.setHighestPrice(biddingAmount);
        currentHighestBid.setAtLeast(nextMinimumBidAmount);
        currentHighestBid.setUser(User.builder().id(newAutoBid.getUserId()).build());
        highestBidStore.store(currentHighestBid);
        return nextMinimumBidAmount;
    }

    public long updateHighestBidToExistingAutoBid(AutoBidDto existingAutoBid, long biddingAmount, HighestBid currentHighestBid) {
        long nextMinimumBidAmount = calculateNextMinimumBidAmount(biddingAmount);
        currentHighestBid.setHighestPrice(biddingAmount);
        currentHighestBid.setUser(User.builder().id(existingAutoBid.getUserId()).build());
        currentHighestBid.setAtLeast(nextMinimumBidAmount);
        highestBidStore.store(currentHighestBid);
        return nextMinimumBidAmount;
    }

    public void registerNewAutoBid(AutoBidCommand newAutoBid, AutoBidDto existingAutoBid) {
        if (existingAutoBid.getStorageType() == StorageType.Main) {
            AutoBid updatedAutoBid = existingAutoBid.getAutoBidMain();
            updatedAutoBid.setUser(User.builder().id(newAutoBid.getUserId()).build());
            updatedAutoBid.setLimit(newAutoBid.getLimit());
            autoBidStore.store(updatedAutoBid);

        } else if (existingAutoBid.getStorageType() == StorageType.Cache) {
            AutoBidCache updatedAutoBid = existingAutoBid.getAutoBidCache();
            updatedAutoBid.setUserId(newAutoBid.getUserId());
            updatedAutoBid.setLimit(newAutoBid.getLimit());
            autoBidStore.storeInCache(updatedAutoBid);

            autoBidStore.update(newAutoBid.getAuctionId(), newAutoBid.getUserId(), newAutoBid.getLimit());
        }
    }

    public void registerExistingAutoBidInCache(AutoBidDto existingAutoBid) {
        if (existingAutoBid.getStorageType() == StorageType.Cache) {
            return;
        }
        AutoBidCache initAutoBidCache = existingAutoBid.toAutoBidCacheEntity();
        autoBidStore.storeInCache(initAutoBidCache);
    }

    public void deleteExistingAutoBid(AutoBidDto existingAutoBid) {
        if (existingAutoBid.getStorageType() == StorageType.Main) {
            autoBidStore.deleteByAuctionId(existingAutoBid.getAuctionId());
        } else if (existingAutoBid.getStorageType() == StorageType.Cache) {
            autoBidStore.deleteByAuctionIdInCache(existingAutoBid.getAuctionId());
            autoBidStore.deleteByAuctionId(existingAutoBid.getAuctionId());
        }
    }

    public long calculateNextMinimumBidAmount(long amount) {
        int increment = IncrementUtil.getIncrement(amount);
        long nextMinimumBidAmount = amount + increment;
        return nextMinimumBidAmount;
    }
}
