package com.sparta.delivery.menu.service;

import com.sparta.delivery.menu.dto.request.MenuRequest;
import com.sparta.delivery.menu.dto.response.MenuResponse;
import com.sparta.delivery.menu.entity.Menu;
import com.sparta.delivery.menu.repository.MenuRepository;
import com.sparta.delivery.user.dto.response.UserResponse;
import com.sparta.delivery.user.entity.User;
import com.sparta.delivery.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class MenuService {
    private final MenuRepository menuRepository;
    private final UserRepository userRepository;

    // 3. 메뉴 등록
    @Transactional
    public MenuResponse create(String username, MenuRequest request) {
        User owner = userRepository.findByUsername(username).orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));

        Menu menu = new Menu(owner, request.getName(), request.getPrice(), request.getDescription());
        Menu savedMenu = menuRepository.save(menu);

        return new MenuResponse(savedMenu);
    }

    // 4. 메뉴 목록 조회
    public List<MenuResponse> getMenus() {
        return menuRepository.findAllByDeletedFalse().stream()
                .map(MenuResponse::new)
                .toList();
    }

    // 5. 메뉴 단건 조회
    public MenuResponse getMenu(Long menuId) {
        Menu menu = findMenuOrThrow(menuId);
        return new MenuResponse(menu);
    }

    // 6. 메뉴 수정
    @Transactional
    public MenuResponse update(String username, Long menuId, MenuRequest request) {
        Menu menu = findMenuOrThrow(menuId);
        validateOwner(menu, username);
        menu.update(request.getName(), request.getPrice(), request.getDescription());

        return new MenuResponse(menu);
    }

    // 7. 메뉴 삭제
    @Transactional
    public void delete(String username, Long menuId) {
        Menu menu = findMenuOrThrow(menuId);
        validateOwner(menu, username);
        menu.softDelete();
    }

    // ---------------- 공통 메서드 ------------------

    // 메뉴존재 확인(404)
    private Menu findMenuOrThrow(Long menuId) {
        return menuRepository.findByIdAndDeletedFalse(menuId).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "메뉴를 찾을 수 없습니다."));
    }

    // 본인메뉴인지 확인(403)
    private void validateOwner(Menu menu, String username) {
        if (!menu.getOwner().getUsername().equals(username)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "본인 메뉴만 수정/삭제할 수 있습니다.");
        }
    }
}