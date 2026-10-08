package com.sparta.delivery.menu.dto.response;

import com.sparta.delivery.menu.entity.Menu;
import lombok.Getter;

@Getter
public class MenuResponse {
    private final Long id;
    private final Long ownerId;
    private final String name;
    private final Integer price;
    private final String description;

    public MenuResponse(Menu menu) {
        this.id = menu.getId();
        this.ownerId = menu.getOwner().getId(); // getId()호출하는 순간, LAZY로설정해둔 owner가 실제DB에서 조회됨(지연로딩 발동)
        this.name = menu.getName();
        this.price = menu.getPrice();
        this.description = menu.getDescription();
    }
}