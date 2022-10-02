package com.mine.domain.auction;

import com.mine.domain.bid.HighestBid;
import com.mine.domain.user.User;
import lombok.*;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "auction")
public class Auction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    private String title;

    private long startingPrice;

    private ZonedDateTime closingTime;

    @Setter
    @OneToOne(mappedBy = "auction")
    private HighestBid highestBid;
}