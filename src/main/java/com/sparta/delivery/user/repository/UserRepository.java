package com.sparta.delivery.user.repository;

import com.sparta.delivery.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    // 회원가입시 아이디 중복체크용 (409 판단에 씀)
    boolean existsByUsername(String username);

    // 로그인시 아이디로 사용자 찾을때 씀
    Optional<User> findByUsername(String username);
}