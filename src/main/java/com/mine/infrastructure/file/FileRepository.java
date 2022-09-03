package com.mine.infrastructure.file;

import com.mine.domain.file.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileRepository extends JpaRepository<File, Long> {

    List<File> findAllByAuction_Id(Long auctionId);
}
