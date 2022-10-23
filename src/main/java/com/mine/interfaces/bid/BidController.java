package com.mine.interfaces.bid;

import com.mine.application.bid.BidFacade;
import com.mine.domain.bid.AutoBidCommand;
import com.mine.domain.bid.BidCommand;
import com.mine.domain.bid.BidInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auction/{auctionId}")
public class BidController {

    private final BidFacade bidFacade;

    @PostMapping("/bid")
    public ResponseEntity<BidDto.BidResponse> bid(@PathVariable long auctionId, @RequestBody BidDto.BidRequest request) {
        BidCommand command = request.toCommand(auctionId);
        BidInfo bidInfo = bidFacade.bid(command);
        BidDto.BidResponse response = new BidDto.BidResponse(bidInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auto-bid")
    public ResponseEntity<BidDto.BidResponse> autoBid(@PathVariable long auctionId, @RequestBody BidDto.AutoBidRequest request) {
        AutoBidCommand command = request.toCommand(auctionId);
        BidInfo bidInfo = bidFacade.autoBid(command);
        BidDto.BidResponse response = new BidDto.BidResponse(bidInfo);
        return ResponseEntity.ok(response);
    }
}
