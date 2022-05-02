package com.mine.infrastructure.user;

import com.mine.domain.user.User;
import com.mine.domain.user.UserStore;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserStoreImpl implements UserStore {

    private final UserRepository userRepository;

    @Override
    public User store(User initUser) {
        return userRepository.save(initUser);
    }
}
