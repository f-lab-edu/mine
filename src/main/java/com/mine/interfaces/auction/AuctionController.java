package com.mine.interfaces.auction;

import com.mine.application.auction.AuctionFacade;
import com.mine.domain.auction.AuctionCommand;
import com.mine.domain.auction.AuctionInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction")
public class AuctionController {

    private final AuctionFacade auctionFacade;

    @PostMapping
    public ResponseEntity<AuctionDto.CreateAuctionResponse> createAuction(@Valid @RequestPart AuctionDto.CreateAuctionRequest request, @RequestPart MultipartFile[] images) {
        AuctionCommand command = request.toCommand(images);
        AuctionInfo auctionInfo = auctionFacade.createAuction(command);
        AuctionDto.CreateAuctionResponse response = new AuctionDto.CreateAuctionResponse(auctionInfo);
        return ResponseEntity.ok(response);
    }
}
