package com.sparta.delivery.menu.controller;

import com.sparta.delivery.menu.dto.request.MenuRequest;
import com.sparta.delivery.menu.dto.response.MenuResponse;
import com.sparta.delivery.menu.service.MenuService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    // 3. 메뉴 등록
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@AuthenticationPrincipal String username, @Valid @RequestBody MenuRequest request) {
        MenuResponse response = menuService.create(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 4. 메뉴목록 조회
    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus() {
        return ResponseEntity.ok(menuService.getMenus());
    }

    // 5. 메뉴 단건 조회
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getMenu(menuId));
    }

    // 6. 메뉴 수정
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@AuthenticationPrincipal String username, @PathVariable Long menuId, @Valid @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.update(username, menuId, request));
    }

    // 7. 메뉴 삭제
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@AuthenticationPrincipal String username, @PathVariable Long menuId) {
        menuService.delete(username, menuId);
        return ResponseEntity.noContent().build();
    }
}