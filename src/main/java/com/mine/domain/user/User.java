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

    @Builder
    public User(String signinUserId, String password, String email) {
        this.signinUserId = signinUserId;
        this.password = password;
        this.email = email;
    }

    public User(Long id) {
        this.id = id;
    }
}

