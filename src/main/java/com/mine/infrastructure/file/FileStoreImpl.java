package com.mine.infrastructure.file;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.mine.domain.file.File;
import com.mine.domain.file.FileStore;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class FileStoreImpl implements FileStore {

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;
    private final AmazonS3Client amazonS3Client;
    private final FileRepository fileRepository;

    @Override
    public void storeToS3(MultipartFile[] files, List<String> keyList) {
        for (int i = 0; i < files.length; i++) {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(files[i].getSize());
            objectMetadata.setContentType(files[i].getContentType());

            try {
                amazonS3Client.putObject(new PutObjectRequest(bucket, keyList.get(i), files[i].getInputStream(), objectMetadata).withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void storeToDatabase(List<File> initFileList) {
        fileRepository.saveAll(initFileList);
    }
}
