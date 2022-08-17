package com.mine.application.user;

import com.mine.domain.user.TokenInfo;
import com.mine.domain.user.UserCommand;
import com.mine.domain.user.UserInfo;
import com.mine.domain.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserFacade {

    private final UserService userService;

    public UserInfo signUpUser(UserCommand command) {
        return userService.signUpUser(command);
    }

    public TokenInfo signIn(UserCommand command) {
        return userService.signIn(command);
    }
}
