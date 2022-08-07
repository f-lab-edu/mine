package com.mine.infrastructure.user;

import com.mine.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsBySigninUserId(String signinUserId);

    Optional<User> findBySigninUserId(String signinUserId);
}
