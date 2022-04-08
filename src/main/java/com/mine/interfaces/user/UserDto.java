package com.mine.interfaces.user;

import com.mine.domain.user.UserCommand;
import com.mine.domain.user.UserInfo;
import lombok.Getter;
import lombok.Setter;

/**
 * static inner class를 사용하여 DTO class 내부에 request 및 response class를 그룹화한다.
 */
public class UserDto {

    @Setter
    public static class SignupRequest {

        private String userId;
        private String password;
        private String email;

        public UserCommand toCommand() {
            return UserCommand.builder()
                    .userId(this.userId)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
    }

    @Getter
    public static class SignupResponse {

        private final String userId;
        private final String password;
        private final String email;

        public SignupResponse(UserInfo userInfo) {
            this.userId = userInfo.getUserId();
            this.password = userInfo.getPassword();
            this.email = userInfo.getEmail();
        }
    }
}
