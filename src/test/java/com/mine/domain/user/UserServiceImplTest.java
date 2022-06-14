package com.mine.domain.user;

import com.mine.common.exception.EntityExistsException;
import com.mine.config.SecurityConfig;
import com.mine.jwt.TokenProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.stubbing.OngoingStubbing;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.test.context.support.WithSecurityContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.web.WebAppConfiguration;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
//@ContextConfiguration(classes = SecurityConfig.class)
//@WebAppConfiguration
//@WithSecurityContext()
class UserServiceImplTest {

    @Mock
    UserReader userReader;
    @Mock
    UserStore userStore;
    @Mock
    PasswordEncoder passwordEncoder;

//    @Mock
    /*ObjectPostProcessor<Object> objectPostProcessor = new ObjectPostProcessor<Object>() {
    @Override
    public <O> O postProcess(O object) {
        return object;
    }
};*/

    //    @Autowired
//    @Mock
//    AuthenticationManagerBuilder authenticationManagerBuilder = new AuthenticationManagerBuilder(objectPostProcessor);
    //    @Autowired
    /*AuthenticationManager authenticationManager = new AuthenticationManager() {
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authentication;
    }
};*/

//    @Autowired
//    AuthenticationManager authenticationManager;

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
                .userId("tester")
                .password("password")
                .email("tester@mine.com")
                .build();
    }

    @Test
    @DisplayName("회원가입 성공")
    void signUpUser() {
        when(passwordEncoder.encode(any())).thenReturn("encodedPassword");

        User savedUser = User.builder()
                .userId("tester")
                .password(passwordEncoder.encode("password"))
                .email("tester@mine.com")
                .authority(Authority.ROLE_USER)
                .build();

        when(userReader.exists(command.getUserId())).thenReturn(false);
        when(userStore.store(any())).thenReturn(savedUser);

        UserInfo userInfo = userService.signUpUser(command);

        assertEquals(command.getUserId(), userInfo.getUserId());
    }

    @Test
    @DisplayName("회원가입 실패_중복 아이디 예외 발생")
    void throwWhenDuplicateUserId() {
        when(userReader.exists(command.getUserId())).thenReturn(true);

        EntityExistsException e = assertThrows(EntityExistsException.class, () -> userService.signUpUser(command));

        assertEquals("이미 존재하는 엔티티입니다.", e.getMessage());
    }

    @Test
    @DisplayName("로그인 성공")
    void signIn() {
        TokenInfo tokenInfo = TokenInfo.builder()
                .grantType("bearer")
                .accessToken("eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiI3IiwiYXV0aCI6IlJPTEVfVVNFUiIsImV4cCI6MTY1NDE4ODUxOH0.UmjQeuh-IzADEPV5Oqb9EqRpptNF3FWo-Fu6Oj86vKCHwN6S4q9Ez98ptqeqRVpOr9wb9EsV-8jzR37Bz3PSPQ")
                .accessTokenExpiresIn(1654188518627L)
                .build();


        System.out.println(authenticationManagerBuilder.toString());

//        when(authenticationManagerBuilder.getObject()).thenReturn(any()).then(authenticationManagerBuilder.getObject().authenticate(any())).thenReturn(any());
//        when(authenticationManagerBuilder.getObject()).thenReturn(any());
//        when(authenticationManagerBuilder.getObject().authenticate(any())).thenReturn(any());
//        when(authenticationManager.authenticate(command.toAuthentication())).thenReturn(null);

//        when(authenticationManagerBuilder.getObject()).thenReturn(mock(AuthenticationManager.class));
//        when(authenticationManagerBuilder.getObject().authenticate(any())).thenReturn(any());
//        when(authenticationManagerBuilder.getObject()).thenReturn(authentication -> authentication);



        AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
        System.out.println(authenticationManager.toString());
// 136 .. .then(authenticationManager);
// 138 .. when(authenticationManager.authenticate(any()).thenReturn(any());


        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
//        when(authenticationManagerBuilder.getObject()).thenReturn(authenticationManager);
        when(authenticationManagerBuilder.getObject().authenticate(any())).thenReturn(any());


        when(tokenProvider.generateTokenInfo(any())).thenReturn(tokenInfo);

        TokenInfo resultTokenInfo = userService.signIn(command);

        assertEquals(tokenInfo.getAccessToken(), resultTokenInfo.getAccessToken());
    }
}