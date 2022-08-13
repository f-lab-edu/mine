package com.mine.domain.file;

import com.mine.domain.auction.Auction;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final FileStore fileStore;

    @Override
    public void uploadFiles(Long auctionId, MultipartFile[] files) {
        List<String> keyList = new ArrayList<>();
        List<File> initFileList = new ArrayList<>();

        for (int i = 0; i < files.length; i++) {
            String fileName = UUID.randomUUID() + files[i].getOriginalFilename();
            String key = "catalog/" + auctionId + "/" + fileName;

            keyList.add(key);
            initFileList.add(File.builder()
                    .auction(new Auction(auctionId))
                    .bucket(bucket)
                    .s3Key(key)
                    .fileOrder(i + 1)
                    .build());
        }

        fileStore.storeToS3(files, keyList);
        fileStore.storeToDatabase(initFileList);
    }
}
