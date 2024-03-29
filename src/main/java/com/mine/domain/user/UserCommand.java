package com.mine.domain.user;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
public class UserCommand {

    @Getter
    private final String signinUserId;
    private final String password;
    private final String email;

    public User toEntity(PasswordEncoder passwordEncoder) {
        return User.builder()
                .signinUserId(this.signinUserId)
                .password(passwordEncoder.encode(this.password))
                .email(this.email)
                .authority(Authority.ROLE_USER)
                .build();
    }

    public UsernamePasswordAuthenticationToken toAuthentication() {
        return new UsernamePasswordAuthenticationToken(this.signinUserId, this.password);
    }
}