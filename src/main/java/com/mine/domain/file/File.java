package com.mine.domain.file;

import com.mine.domain.auction.Auction;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "auction_id")
    private Auction auction;

    private String bucket;

    @Getter
    @Column(name = "s3_key")
    private String s3Key;

    private int fileOrder;

    @Builder
    public File(Auction auction, String bucket, String s3Key, int fileOrder) {
        this.auction = auction;
        this.bucket = bucket;
        this.s3Key = s3Key;
        this.fileOrder = fileOrder;
    }
}