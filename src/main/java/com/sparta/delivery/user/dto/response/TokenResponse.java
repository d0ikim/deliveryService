package com.sparta.delivery.user.dto.response;

import lombok.Getter;

@Getter
public class TokenResponse {    // 응답형태를 {"token": "어쩌구.."}처럼 JSON객체로 감싸기위한 래퍼
    private final String token;

    public TokenResponse(String token) {
        this.token = token;
    }
}