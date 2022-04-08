package com.mine.domain.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public class UserCommand {

    @Getter
    private final String userId;
    private final String password;
    private final String email;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .userId(this.userId)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .build();
    }
}