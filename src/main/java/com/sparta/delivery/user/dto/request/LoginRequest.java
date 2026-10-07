package com.sparta.delivery.user.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class LoginRequest {
    @NotBlank   // 빈 값 요청을 막기
    private String username;

    @NotBlank
    private String password;
}