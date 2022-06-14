package com.mine.domain.user;

import java.util.Optional;

public interface UserReader {

    boolean exists(String userId);

    Optional<User> findByUserId(String userId);
}
