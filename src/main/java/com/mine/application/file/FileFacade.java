package com.mine.application.file;

import com.mine.domain.file.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileFacade {

    private final FileService fileService;

    public void uploadFiles(long auctionId, MultipartFile[] files) {
        fileService.uploadFiles(auctionId, files);
    }
}
