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

    @Builder
    public User(String userId, String password, String email) {
        if (userId == null) {
            throw new RuntimeException("empty userId");
        }
        if (password == null) {
            throw new RuntimeException("empty password");
        }
        if (email == null) {
            throw new RuntimeException("empty email");
        }

        this.userId = userId;
        this.password = password;
        this.email = email;
    }
}

