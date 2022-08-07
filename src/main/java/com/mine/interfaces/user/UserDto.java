package com.mine.interfaces.user;

import com.mine.domain.user.UserCommand;
import com.mine.domain.user.UserInfo;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class UserDto {

    @Setter
    public static class SignupRequest {

        @NotBlank
        @Size(min = 5, max = 20)
        private String signinUserId;

        @NotBlank
        @Size(min = 8, max = 16)
        private String password;

        @Email
        private String email;

        public UserCommand toCommand() {
            return UserCommand.builder()
                    .signinUserId(this.signinUserId)
                    .password(this.password)
                    .email(this.email)
                    .build();
        }
    }

    @Getter
    public static class SignupResponse {

        private final Long id;
        private final String signinUserId;
        private final String password;
        private final String email;

        public SignupResponse(UserInfo userInfo) {
            this.id = userInfo.getId();
            this.signinUserId = userInfo.getSigninUserId();
            this.password = userInfo.getPassword();
            this.email = userInfo.getEmail();
        }
    }

    @Setter
    public static class SigninRequest {

        private String signinUserId;
        private String password;

        public UserCommand toCommand() {
            return UserCommand.builder()
                    .signinUserId(this.signinUserId)
                    .password(this.password)
                    .build();
        }
    }
}
