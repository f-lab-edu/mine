package com.mine.application.auction;

import com.mine.application.file.FileFacade;
import com.mine.domain.auction.AuctionCommand;
import com.mine.domain.auction.AuctionInfo;
import com.mine.domain.auction.AuctionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URL;
import java.util.List;

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

    public AuctionInfo showCatalog(Long auctionId) {
        AuctionInfo auctionInfo = auctionService.showCatalog(auctionId);
        List<URL> fileUrls = fileFacade.downloadFiles(auctionId);
        auctionInfo.setFileUrls(fileUrls);
        return auctionInfo;
    }
}