package com.mine.interfaces.user;

import com.mine.application.user.UserFacade;
import com.mine.domain.user.UserCommand;
import com.mine.domain.user.UserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {

    private final UserFacade userFacade;

    @PostMapping
    public ResponseEntity<UserDto.SignupResponse> signUpUser(@RequestBody UserDto.SignupRequest request) {
        UserCommand command = request.toCommand();
        UserInfo userInfo = userFacade.signUpUser(command);
        UserDto.SignupResponse response = new UserDto.SignupResponse(userInfo);
        return ResponseEntity.ok(response);
    }
}
