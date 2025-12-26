package com.example.plantmanager.controller;

import com.example.plantmanager.dto.*;
import com.example.plantmanager.security.JwtUtil;
import com.example.plantmanager.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest req) {
        return ResponseEntity.ok(authService.register(req));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest req) {
        return ResponseEntity.ok(authService.login(req));
    }

    // Google One Tap / GSI credential flow
    @PostMapping("/google")
    public ResponseEntity<AuthResponse> google(@Valid @RequestBody GoogleAuthRequest req) {
        return ResponseEntity.ok(authService.loginWithGoogleCredential(req));
    }

    // Authorization Code callback (optional if using OAuth2 code flow)
    @GetMapping("/google/callback")
    public ResponseEntity<AuthResponse> googleCallback(@RequestParam("code") String code) {
        return ResponseEntity.ok(authService.loginWithAuthorizationCode(code));
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(Authentication authentication) {
        String email = authentication.getName();
        return ResponseEntity.ok(authService.me(email));
    }
}
