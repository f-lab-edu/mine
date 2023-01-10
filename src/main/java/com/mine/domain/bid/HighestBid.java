package com.mine.domain.bid;

import com.mine.domain.auction.Auction;
import com.mine.domain.user.User;
import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "highest_bid")
public class HighestBid {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "auction_id")
    private Auction auction;

    @Setter
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Setter
    private long highestBidAmount;

    @Setter
    private long incrementalBidAmount;
}
