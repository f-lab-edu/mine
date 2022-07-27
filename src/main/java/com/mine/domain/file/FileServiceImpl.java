package com.mine.domain.file;

import com.mine.domain.auction.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final FileStore fileStore;

    @Override
    public void uploadFiles(Long auctionId, MultipartFile[] files) {
        List<String> keyList = new ArrayList<>();
        List<File> initFileList = new ArrayList<>();

        for (MultipartFile file : files) {
            String fileName = UUID.randomUUID() + file.getOriginalFilename();
            String key = "catalog/" + auctionId + "/" + fileName;

            keyList.add(key);
            initFileList.add(File.builder()
                    .auction(new Auction(auctionId))
                    .s3Key(key)
                    .build());
        }

        fileStore.storeToS3(files, keyList);
        fileStore.storeToDatabase(initFileList);
    }
}
