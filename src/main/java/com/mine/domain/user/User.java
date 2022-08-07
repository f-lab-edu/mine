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

    private String signinUserId;

    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    private Authority authority;

    @Builder
    public User(String signinUserId, String password, String email, Authority authority) {
        this.signinUserId = signinUserId;
        this.password = password;
        this.email = email;
        this.authority = authority;
    }
}