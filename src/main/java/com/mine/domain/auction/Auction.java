package com.mine.domain.auction;

import com.mine.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Getter
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

    private Long startingPrice;

    private ZonedDateTime closingTime;

    @Builder
    public Auction(User user, String title, Long startingPrice, ZonedDateTime closingTime) {
        this.user = user;
        this.title = title;
        this.startingPrice = startingPrice;
        this.closingTime = closingTime;
    }
}