package com.mine.domain.file;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {

    void uploadFiles(long auctionId, MultipartFile[] files);
}
