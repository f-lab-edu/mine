package com.mine.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfo signUpUser(UserCommand command) {
        userReader.checkUserExists(command.getUserId());
        User initUser = command.toEntity(passwordEncoder);
        User user = userStore.store(initUser);
        return new UserInfo(user);
    }
}