package com.mine.domain.auction;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Service
@RequiredArgsConstructor
public class AuctionServiceImpl implements AuctionService {

    private final AuctionStore auctionStore;

    @Override
    public AuctionInfo createAuction(AuctionCommand command) {
        ZonedDateTime closingTime = ZonedDateTime.now(ZoneId.of("UTC")).plusDays(command.getDuration());    // 경매 지속일을 더한 날짜/시간
        Auction initAuction = command.toEntity(closingTime);
        Auction auction = auctionStore.store(initAuction);
        System.out.println("11");
        uploadImages(auction.getId(), command.getImages());
        System.out.println("22");
        return new AuctionInfo(auction);
    }

    @Override
    public void uploadImages(Long auctionId, MultipartFile[] images) {
        String basePath = "./src/main/resources/image";
        int length = images.length;

        for (int i = 0; i < length; i++) {
            String targetDir = String.valueOf(Path.of(basePath, String.valueOf(auctionId)));
            String newImageName = String.valueOf(i);
            File dirExists = new File(targetDir);

            if (!dirExists.exists()) {
                dirExists.mkdirs();
            }

            try {
                images[i].transferTo(Path.of(targetDir, newImageName));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
