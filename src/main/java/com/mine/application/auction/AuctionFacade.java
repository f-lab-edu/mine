package com.mine.application.auction;

import com.mine.application.file.FileFacade;
import com.mine.domain.auction.AuctionCommand;
import com.mine.domain.auction.AuctionInfo;
import com.mine.domain.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuctionFacade {

    private final AuctionService auctionService;
    private final FileFacade fileFacade;

    @Transactional
    public AuctionInfo createAuction(AuctionCommand command) {
        AuctionInfo info = auctionService.createAuction(command);
        fileFacade.uploadFiles(info.getId(), command.getImages());
        return info;
    }
}