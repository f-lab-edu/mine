package com.mine.common.response;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {

    COMMON_SYSTEM_ERROR("일시적인 오류가 발생했습니다. 잠시 후 다시 시도해주세요."), // 장애 상황
    COMMON_INVALID_PARAMETER("요청한 값이 올바르지 않습니다."),
    COMMON_ENTITY_NOT_FOUND("존재하지 않는 엔티티입니다."),
    COMMON_ILLEGAL_STATUS("잘못된 상태 값입니다."),
    COMMON_ENTITY_EXISTS("이미 존재하는 엔티티입니다."),
    COMMON_HIGHEST_BID_NOT_UPDATED("입찰에 실패했습니다. 입찰 가능한 최소 금액 이상으로 다시 입찰해 주세요."),
    COMMON_AUCTION_ALREADY_CLOSED("이미 마감된 경매입니다. 더 이상 입찰할 수 없습니다.");

    private final String errorMessage;

    public String getErrorMessage(Object... arg) {
        return String.format(errorMessage, arg);
    }
}
