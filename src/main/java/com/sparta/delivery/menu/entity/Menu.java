package com.sparta.delivery.menu.entity;

import com.sparta.delivery.global.entity.BaseEntity;
import com.sparta.delivery.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "menus")
public class Menu extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)  // 메뉴여러개가 사장님1명을 가리킨다(N:1), 진짜필요할때만 User도 같이조회하도록 지연로딩
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;         // 메뉴의 주인(요청한 사장님)

    @Column(nullable = false)
    private String name;        // 메뉴 이름

    @Column(nullable = false)
    private Integer price;      // 메뉴 가격

    private String description; // 메뉴 설명(선택)

    private boolean deleted = false;    // 메뉴 '삭제됨' 여부 (soft delete)

    public Menu(User owner, String name, Integer price, String description) {
        this.owner = owner;
        this.name = name;
        this.price = price;
        this.description = description;
        this.deleted = false;
    }

    public void update(String name, Integer price, String description) {
        this.name = name;
        this.price = price;
        this.description = description;
    }

    public void softDelete() {
        this.deleted = true;
    }
}