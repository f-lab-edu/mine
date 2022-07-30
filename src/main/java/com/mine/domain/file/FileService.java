package com.mine.domain.file;

import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.List;

public interface FileService {

    void uploadFiles(long auctionId, MultipartFile[] files);

    List<URL> downloadFiles(Long auctionId);
}
