package com.mine.infrastructure.user;

import com.mine.domain.user.UserReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import javax.persistence.EntityExistsException;

@Component
@RequiredArgsConstructor
public class UserReaderImpl implements UserReader {

    private final UserRepository userRepository;

    @Override
    public void checkUserExists(String userId) {
        if (userRepository.existsByUserId(userId)) {
            throw new EntityExistsException("ID already exists");
        }
    }
}
