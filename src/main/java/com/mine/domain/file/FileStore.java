package com.mine.domain.file;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileStore {

    void storeToS3(MultipartFile[] files, List<String> keyList);

    void storeToDatabase(List<File> initFileList);
}
