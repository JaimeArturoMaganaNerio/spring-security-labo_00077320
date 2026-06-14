package com.server.app.controllers;

import com.server.app.config.JsonWebToken;
import com.server.app.dto.auth.AuthResponse;
import com.server.app.dto.auth.LoginDTO;
import com.server.app.dto.auth.UpdatePasswordDTO;
import com.server.app.dto.user.UserCreateDto;
import com.server.app.dto.user.UserUpdateDto;
import com.server.app.entities.User;
import com.server.app.services.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

    private final UserService userService;
    private final JsonWebToken jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginDTO dto) {
        User user = userService.login(dto.getUsername(), dto.getPassword());
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@Valid @RequestBody UserCreateDto dto) {
        // La guía pide Rol ADMIN por defecto en el signup (Id 1 en tu script.sql)
        if (dto.getRole() == null) {
            dto.setRole(1L);
        }
        User user = userService.create(dto);
        String token = jwtUtil.createToken(user);
        return ResponseEntity.ok(new AuthResponse(token, user));
    }

    @GetMapping("/profile")
    public ResponseEntity<User> getProfile(@AuthenticationPrincipal User user) {
        // Retorna solo la data del usuario autenticado
        return ResponseEntity.ok(user);
    }

    @PutMapping("/update/profile")
    public ResponseEntity<AuthResponse> updateProfile(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UserUpdateDto dto) {

        User updatedUser = userService.updateUser(user.getId(), dto);
        String token = jwtUtil.createToken(updatedUser);
        return ResponseEntity.ok(new AuthResponse(token, updatedUser));
    }

    @PutMapping("/update/password")
    public ResponseEntity<User> updatePassword(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody UpdatePasswordDTO dto) {

        User updatedUser = userService.updatePassword(
                user.getId(),
                dto.getOldpassword(),
                dto.getNewpassword(),
                dto.getConfirmpassword()
        );
        return ResponseEntity.ok(updatedUser);
    }
}