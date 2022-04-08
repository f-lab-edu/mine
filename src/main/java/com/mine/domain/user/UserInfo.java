package com.mine.domain.user;

import lombok.Getter;

@Getter
public class UserInfo {

    private final Long id;
    private final String userId;
    private final String password;
    private final String email;

    public UserInfo(User user) {
        this.id = user.getId();
        this.userId = user.getUserId();
        this.password = user.getPassword();
        this.email = user.getEmail();
    }
}
