package com.mine.interfaces.user;

import com.mine.application.user.UserFacade;
import com.mine.domain.user.*;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping("/signup")
    public ResponseEntity<UserDto.SignupResponse> signUpUser(@Valid @RequestBody UserDto.SignupRequest request) {
        UserCommand command = request.toCommand();
        UserInfo userInfo = userFacade.signUpUser(command);
        UserDto.SignupResponse response = new UserDto.SignupResponse(userInfo);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signin")
    public ResponseEntity<TokenDto.SigninResponse> signIn(@RequestBody UserDto.SigninRequest request) {
        UserCommand command = request.toCommand();
        TokenInfo tokenInfo = userFacade.signIn(command);
        TokenDto.SigninResponse response = new TokenDto.SigninResponse(tokenInfo);
        return ResponseEntity.ok(response);
    }
}
