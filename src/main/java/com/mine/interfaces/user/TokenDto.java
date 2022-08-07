package com.mine.interfaces.user;

import com.mine.domain.user.TokenInfo;
import lombok.Getter;

public class TokenDto {

    @Getter
    public static class SigninResponse {

        private final String grantType;
        private final String accessToken;
        private final Long accessTokenExpiresIn;

        public SigninResponse(TokenInfo tokenInfo) {
            this.grantType = tokenInfo.getGrantType();
            this.accessToken = tokenInfo.getAccessToken();
            this.accessTokenExpiresIn = tokenInfo.getAccessTokenExpiresIn();
        }
    }
}