package com.mine.interfaces.bid;

import com.mine.application.bid.BidFacade;
import com.mine.domain.bid.BidCommand;
import com.mine.domain.bid.BidInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/bid")
public class BidController {

    private final BidFacade bidFacade;

    @PostMapping
    public ResponseEntity<BidDto.BidResponse> bid(@RequestBody BidDto.BidRequest request) {
        BidCommand command = request.toCommand();
        BidInfo bidInfo = bidFacade.bid(command);
        BidDto.BidResponse response = new BidDto.BidResponse(bidInfo);
        return ResponseEntity.ok(response);
    }
}
