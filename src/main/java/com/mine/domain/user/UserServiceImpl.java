package com.mine.domain.user;

import com.mine.common.exception.EntityExistsException;
import com.mine.jwt.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserReader userReader;
    private final UserStore userStore;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final TokenProvider tokenProvider;

    @Override
    public UserInfo signUpUser(UserCommand command) {
        if (userReader.exists(command.getUserId())) {
            throw new EntityExistsException();
        }
        User initUser = command.toEntity(passwordEncoder);
        User user = userStore.store(initUser);
        return new UserInfo(user);
    }

    @Transactional
    @Override
    public TokenInfo signIn(UserCommand command) {
        // Signin ID/PW를 기반으로 AuthenticationToken 생성
        UsernamePasswordAuthenticationToken authenticationToken = command.toAuthentication();

        // 실제 검증(비밀번호 체크)이 이루어지는 부분
        // authenticate 메서드가 실행될 때 CustomUserDetailsService에 만든 loadUserByUsername 메서드 실행
        Authentication authentication = authenticationManagerBuilder.getObject().authenticate(authenticationToken);

        // 인증 정보를 기반으로 JWT 토큰 생성
        TokenInfo tokenInfo = tokenProvider.generateTokenInfo(authentication);

        // 토큰 반환
        return tokenInfo;
    }
}