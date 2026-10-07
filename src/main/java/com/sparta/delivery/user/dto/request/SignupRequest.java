package com.sparta.delivery.user.dto.request;

import com.sparta.delivery.user.entity.Role;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class SignupRequest {    // 입력전용 DTO
    @NotBlank
    @Size(min = 4, max = 20)
    private String username;

    @NotBlank
    @Size(min = 8)
    private String password;

    @NotNull
    private Role role;
}