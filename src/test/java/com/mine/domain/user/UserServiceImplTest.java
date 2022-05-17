package com.mine.domain.user;

import com.mine.common.exception.EntityExistsException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    UserReader userReader;
    @Mock
    UserStore userStore;
    @Mock
    PasswordEncoder passwordEncoder;
    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("회원가입 성공")
    void signUpUser() {
        UserCommand command = UserCommand.builder()
                .userId("tester")
                .password("password")
                .email("tester@mine.com")
                .build();

        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .userId("tester")
                .password(passwordEncoder.encode("password"))
                .email("tester@mine.com")
                .build();

        when(userReader.exists(command.getUserId())).thenReturn(false);
        when(userStore.store(any())).thenReturn(savedUser);

        UserInfo userInfo = userService.signUpUser(command);

        assertEquals(command.getUserId(), userInfo.getUserId());
    }

    @Test
    @DisplayName("회원가입 실패_중복 아이디 예외 발생")
    void throwWhenDuplicateUserId() {
        UserCommand command = UserCommand.builder()
                .userId("tester")
                .password("password")
                .email("tester@mine.com")
                .build();

        when(userReader.exists(command.getUserId())).thenReturn(true);

        EntityExistsException e = assertThrows(EntityExistsException.class, () -> userService.signUpUser(command));
        assertEquals("이미 존재하는 엔티티입니다.", e.getMessage());
    }
}