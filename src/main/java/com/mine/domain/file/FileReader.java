package com.mine.domain.file;

import java.net.URL;
import java.util.List;

public interface FileReader {

    List<File> readAllFromDatabase(Long auctionId);
    URL readFromS3(String key);
}
