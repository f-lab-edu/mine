package com.mine.domain.auction;

import org.springframework.web.multipart.MultipartFile;

public interface AuctionService {

    AuctionInfo createAuction(AuctionCommand command);

    void uploadImages(Long auctionId, MultipartFile[] images);
}
