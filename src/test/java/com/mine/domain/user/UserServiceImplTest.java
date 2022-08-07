package com.mine.domain.user;

import com.mine.common.exception.EntityExistsException;
import com.mine.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
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

    @Mock
    AuthenticationManagerBuilder authenticationManagerBuilder;

    @Mock
    TokenProvider tokenProvider;

    @InjectMocks
    UserServiceImpl userService;

    UserCommand command;

    @BeforeEach
    void setUp() {
        command = UserCommand.builder()
                .signinUserId("tester")
                .password("password")
                .email("leeseowoo.kr@gmail.com")
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpUser() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .signinUserId("tester")
                .password(passwordEncoder.encode("password"))
                .email("tester@mine.com")
                .authority(Authority.ROLE_USER)
                .build();

        when(userReader.exists(command.getSigninUserId())).thenReturn(false);
        when(userStore.store(any())).thenReturn(savedUser);

        UserInfo userInfo = userService.signUpUser(command);

        assertEquals(command.getSigninUserId(), userInfo.getSigninUserId());
    }

    @Test
    @DisplayName("회원가입 실패_중복 아이디 예외 발생")
    void throwWhenDuplicateSigninUserId() {
        when(userReader.exists(command.getSigninUserId())).thenReturn(true);

        EntityExistsException e = assertThrows(EntityExistsException.class, () -> userService.signUpUser(command));

        assertEquals("이미 존재하는 엔티티입니다.", e.getMessage());
    }

    @Test
    @DisplayName("로그인 성공")
    void signIn() {
        TokenInfo generatedTokenInfo = TokenInfo.builder()
                .grantType("bearer")
                .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY1NDE4ODUxOH0.UmjQeuh-IzADEPV5Oqb9EqRpptNF3FWo-Fu6Oj86vKCHwN6S4q9Ez98ptqeqRVpOr9wb9EsV-8jzR37Bz3PSPQ")
                .accessTokenExpiresIn(1654188518627L)
                .build();

        when(authenticationManagerBuilder.getObject()).thenReturn(mock(AuthenticationManager.class));
        when(authenticationManagerBuilder.getObject().authenticate(any())).thenReturn(mock(Authentication.class));
        when(tokenProvider.generateTokenInfo(any())).thenReturn(generatedTokenInfo);

        TokenInfo tokenInfo = userService.signIn(command);

        assertEquals(generatedTokenInfo.getAccessToken(), tokenInfo.getAccessToken());
    }

    @Test
    @DisplayName("로그인 실패")
    void throwWhenSigninFail() {
        UserCommand nonExistentUser = UserCommand.builder()
                .signinUserId("nonexistentuser")
                .password("password")
                .build();

        when(authenticationManagerBuilder.getObject()).thenReturn(mock(AuthenticationManager.class));
        when(authenticationManagerBuilder.getObject().authenticate(any())).thenThrow(BadCredentialsException.class);

        assertThrows(BadCredentialsException.class, () -> userService.signIn(nonExistentUser));
    }
}