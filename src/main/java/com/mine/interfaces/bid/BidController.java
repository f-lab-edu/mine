package com.mine.interfaces.bid;

import com.mine.application.bid.BidFacade;
import com.mine.domain.bid.AutoBidRegisterCommand;
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
    public ResponseEntity<BidDto.BidResponse> manualBid(@PathVariable long auctionId, @RequestBody BidDto.BidRequest request) {
        BidCommand command = request.toCommand(auctionId);
        BidInfo bidInfo = bidFacade.manualBid(command);
        BidDto.BidResponse response = new BidDto.BidResponse(bidInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/auto-bid")
    public ResponseEntity<BidDto.BidResponse> registerNewAutoBid(@PathVariable long auctionId, @RequestBody BidDto.AutoBidRequest request) {
        AutoBidRegisterCommand command = request.toCommand(auctionId);
        BidInfo bidInfo = bidFacade.registerAutoBid(command);
        BidDto.BidResponse response = new BidDto.BidResponse(bidInfo);
        return ResponseEntity.ok(response);
    }
}
