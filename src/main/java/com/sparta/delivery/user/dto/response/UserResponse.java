package com.sparta.delivery.user.dto.response;

import com.sparta.delivery.user.entity.Role;
import com.sparta.delivery.user.entity.User;
import lombok.Getter;

@Getter
public class UserResponse {
    private final Long id;
    private final String username;
    private final Role role;

    public UserResponse(User user) {
        this.id = user.getId();
        this.username = user.getUsername();
        this.role = user.getRole();
    }
}