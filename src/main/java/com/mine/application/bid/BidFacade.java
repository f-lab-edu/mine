package com.mine.application.bid;

import com.mine.domain.bid.*;
import com.mine.infrastructure.bid.RedisPubsubPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BidFacade {

    private final BeanFactory beanFactory;
    private final HighestBidReader highestBidReader;
    private final HighestBidStore highestBidStore;
    private final AutoBidService autoBidService;
    private final RedisPubsubPublisher redisPubsubPublisher;

    @Transactional
    public BidInfo manualBid(BidCommand command) {
        ManualBidServiceImpl manualBidService = beanFactory.getBean("ManualBidService", ManualBidServiceImpl.class);
        HighestBid highestBid = highestBidReader.findByAuctionId(command.getAuctionId());
        BidInfo bidInfo = manualBidService.manualBid(command, highestBid);
        AutoBidDto autoBid = manualBidService.findRegisteredAutoBid(command.getAuctionId());
        if (autoBid.getStorageType() == StorageType.CACHE || autoBid.getStorageType() == StorageType.MAIN_DB) {
            autoBidService.autoBid(autoBid, highestBid);
        }
        highestBidStore.store(highestBid);

        redisPubsubPublisher.convertAndSend(highestBid);
        return bidInfo;
    }

    @Transactional
    public BidInfo registerAutoBid(AutoBidRegisterCommand command) {
        AutoBidRegisterServiceImpl autoBidRegisterService = beanFactory.getBean("AutoBidRegisterService", AutoBidRegisterServiceImpl.class);
        HighestBid highestBid = highestBidReader.findByAuctionId(command.getAuctionId());
        AutoBidDto existingAutoBid = autoBidRegisterService.findRegisteredAutoBid(command.getAuctionId());
        BidInfo bidInfo = autoBidRegisterService.registerAutoBid(command, existingAutoBid, highestBid);
        highestBidStore.store(highestBid);
        return bidInfo;
    }
}
