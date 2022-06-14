package com.mine.domain.user;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenInfo {

    private final String grantType;
    private final String accessToken;
    private final Long accessTokenExpiresIn;
}