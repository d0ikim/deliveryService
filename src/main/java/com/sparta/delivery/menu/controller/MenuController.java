package com.sparta.delivery.menu.controller;

import com.sparta.delivery.menu.dto.request.MenuRequest;
import com.sparta.delivery.menu.dto.response.MenuResponse;
import com.sparta.delivery.menu.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "메뉴", description = "메뉴 등록·조회·수정·삭제 API")
@RestController
@RequestMapping("/api/menus")
@RequiredArgsConstructor
public class MenuController {
    private final MenuService menuService;

    // 3. 메뉴 등록
    @Operation(summary = "메뉴 등록", description = "사장님(OWNER)이 새 메뉴를 등록합니다. 메뉴의 주인은 토큰에서 꺼낸 로그인한 사장님 본인으로 설정됩니다.")
    @PreAuthorize("hasRole('OWNER')")
    @PostMapping
    public ResponseEntity<MenuResponse> createMenu(@AuthenticationPrincipal String username, @Valid @RequestBody MenuRequest request) {
        MenuResponse response = menuService.create(username, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    // 4. 메뉴목록 조회
    @Operation(summary = "메뉴 목록 조회", description = "삭제되지 않은 전체 메뉴 목록을 조회합니다. 로그인하지 않아도 조회할 수 있습니다.")
    @GetMapping
    public ResponseEntity<List<MenuResponse>> getMenus() {
        return ResponseEntity.ok(menuService.getMenus());
    }

    // 5. 메뉴 단건 조회
    @Operation(summary = "메뉴 단건 조회", description = "메뉴 ID로 메뉴 하나의 상세 정보를 조회합니다. 없거나 삭제된 메뉴면 404가 반환됩니다.")
    @GetMapping("/{menuId}")
    public ResponseEntity<MenuResponse> getMenu(@PathVariable Long menuId) {
        return ResponseEntity.ok(menuService.getMenu(menuId));
    }

    // 6. 메뉴 수정
    @Operation(summary = "메뉴 수정", description = "본인이 등록한 메뉴만 수정할 수 있습니다. 다른 사장님의 메뉴를 수정하려고 하면 403이 반환됩니다.")
    @PreAuthorize("hasRole('OWNER')")
    @PatchMapping("/{menuId}")
    public ResponseEntity<MenuResponse> updateMenu(@AuthenticationPrincipal String username, @PathVariable Long menuId, @Valid @RequestBody MenuRequest request) {
        return ResponseEntity.ok(menuService.update(username, menuId, request));
    }

    // 7. 메뉴 삭제
    @Operation(summary = "메뉴 삭제", description = "본인이 등록한 메뉴만 삭제할 수 있습니다. 실제로 DB에서 지우지 않고 삭제 여부만 표시하는 Soft Delete 방식입니다.")
    @PreAuthorize("hasRole('OWNER')")
    @DeleteMapping("/{menuId}")
    public ResponseEntity<Void> deleteMenu(@AuthenticationPrincipal String username, @PathVariable Long menuId) {
        menuService.delete(username, menuId);
        return ResponseEntity.noContent().build();
    }
}