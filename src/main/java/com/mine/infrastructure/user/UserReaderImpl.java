package com.mine.infrastructure.user;

import com.mine.domain.user.User;
import com.mine.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    @Override
    public boolean exists(String userId) {
        return userRepository.existsBySigninUserId(userId);
    }

    @Override
    public Optional<User> findBySigninUserId(String signinUserId) {
        return userRepository.findBySigninUserId(signinUserId);
    }
}
