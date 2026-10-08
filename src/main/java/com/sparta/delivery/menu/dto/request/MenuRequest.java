package com.sparta.delivery.menu.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class MenuRequest {
    @NotBlank
    private String name;

    @NotNull
    @Min(1) // 가격이 1원보다 작으면 400 요구사항
    private Integer price;

    private String description;
}