package com.mine.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String userId, String password, String email, Authority authority) {
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.authority = authority;
    }
}