package com.mine.infrastructure.user;

import com.mine.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

    boolean existsBySigninUserId(String signinUserId);
}
