package com.mine.domain.user;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserInfo signUpUser(UserCommand command) {
        if (userReader.exists(command.getUserId())) {
            throw new EntityExistsException("이미 사용 중인 아이디입니다.");
        }
        User initUser = command.toEntity(passwordEncoder);
        User user = userStore.store(initUser);
        return new UserInfo(user);
    }
}