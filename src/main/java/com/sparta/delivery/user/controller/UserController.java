package com.sparta.delivery.user.controller;

import com.sparta.delivery.user.dto.request.SignupRequest;
import com.sparta.delivery.user.dto.response.UserResponse;
import com.sparta.delivery.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "회원", description = "회원가입 API")
@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @Operation(summary = "회원가입", description = "아이디, 비밀번호, 역할(CUSTOMER 또는 OWNER)을 받아 회원가입합니다. 아이디는 4~20자, 비밀번호는 8자 이상이어야 하며, 비밀번호는 BCrypt로 암호화되어 저장됩니다.")
    @PostMapping
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) { // @Valid : SignupRequest에 걸어둔 @NotBlank, @Size, @NotNull 검증을 여기서 실행시키는 스위치
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}