package com.mine.domain.user;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.EntityNotFoundException;

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
                .userId("TEST_USER")
                .password("test0000password")
                .email("test@mine.com")
                .build();

        when(passwordEncoder.encode(any())).thenReturn("encoded0000password");

        User savedUser = User.builder()
                .userId("TEST_USER")
                .password(passwordEncoder.encode("test0000password"))
                .email("test@mine.com")
                .build();

        doNothing().when(userReader).checkUserExists(command.getUserId());
        when(userStore.store(any())).thenReturn(savedUser);

        UserInfo userInfo = userService.signUpUser(command);

        assertEquals(command.getUserId(), userInfo.getUserId());
    }

    @Test
    @DisplayName("회원가입 실패_중복 아이디 예외 발생")
    void throwWhenDuplicateUserId() {
        UserCommand command = UserCommand.builder()
                .userId("TEST_USER")
                .password("test0000password")
                .email("test0@mine.com")
                .build();

        doNothing().when(userReader).checkUserExists(command.getUserId());

        userReader.checkUserExists(command.getUserId());

        UserCommand command2 = UserCommand.builder()
                .userId("TEST_USER")
                .password("test1111password")
                .email("test1@mine.com")
                .build();

        doThrow(new EntityNotFoundException("ID already exists")).when(userReader).checkUserExists(command2.getUserId());

        EntityNotFoundException e = assertThrows(EntityNotFoundException.class, () -> userReader.checkUserExists(command2.getUserId()));
        assertEquals("ID already exists", e.getMessage());
    }
}