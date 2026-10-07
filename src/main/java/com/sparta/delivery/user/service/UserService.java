package com.sparta.delivery.user.service;

import com.sparta.delivery.user.dto.request.SignupRequest;
import com.sparta.delivery.user.dto.response.UserResponse;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor    // final필드를 받는 생성자를 자동으로 만들어줌
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Transactional  // DB 쓰기작업(save)이 있는 메서드엔 꼭 붙일것!
    public UserResponse signup(SignupRequest request) {
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "이미 존재하는 아이디입니다.");  // 아이디중복(409에러) 표현
        }

        String encodedPassword = passwordEncoder.encode(request.getPassword()); // 비밀번호 BCrypt 암호화(단방향 해시) 저장
        User user = new User(request.getUsername(), encodedPassword, request.getRole());
        User savedUser = userRepository.save(user);

        return new UserResponse(savedUser);
    }
}