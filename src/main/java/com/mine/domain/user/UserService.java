package com.mine.domain.user;

public interface UserService {

    UserInfo signUpUser(UserCommand command);

    TokenInfo signIn(UserCommand command);
}
