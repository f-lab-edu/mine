package com.mine.infrastructure.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.mine.domain.file.File;
import com.mine.domain.file.FileReader;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileReaderImpl implements FileReader {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;

    @Override
    public List<File> readAllFromDatabase(Long auctionId) {
        return fileRepository.findAllByAuction_Id(auctionId);
    }

    @Override
    public URL readFromS3(String key) {
        return amazonS3Client.getUrl(bucket, key);
    }
}
