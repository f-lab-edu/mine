package com.mine.domain.file;

import com.mine.domain.auction.Auction;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@NoArgsConstructor
@Table(name = "file")
public class File {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @Column(name = "s3_key")
    private String s3Key;

    @Builder
    public File(Auction auction, String s3Key) {
        this.auction = auction;
        this.s3Key = s3Key;
    }
}