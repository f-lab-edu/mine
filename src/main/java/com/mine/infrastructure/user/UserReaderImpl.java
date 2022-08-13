package com.mine.infrastructure.user;

import com.mine.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    @Override
    public boolean exists(String signinUserId) {
        return userRepository.existsBySigninUserId(signinUserId);
    }
}
