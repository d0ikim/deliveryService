package com.sparta.delivery.user.controller;

import com.sparta.delivery.user.dto.request.SignupRequest;
import com.sparta.delivery.user.dto.response.UserResponse;
import com.sparta.delivery.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping
    public ResponseEntity<UserResponse> signup(@Valid @RequestBody SignupRequest request) { // @Valid : SignupRequest에 걸어둔 @NotBlank, @Size, @NotNull 검증을 여기서 실행시키는 스위치
        UserResponse response = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}