package com.mine.domain.user;

import lombok.Getter;

@Getter
public class UserInfo {

    private final Long id;
    private final String signinUserId;
    private final String password;
    private final String email;

    public UserInfo(User user) {
        this.id = user.getId();
        this.signinUserId = user.getSigninUserId();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }
}
