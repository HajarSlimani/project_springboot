package com.example.plantmanager.service;

import com.example.plantmanager.dto.*;
import com.example.plantmanager.model.AppUser;
import com.example.plantmanager.model.AuthProvider;
import com.example.plantmanager.model.Role;
import com.example.plantmanager.repository.RoleRepository;
import com.example.plantmanager.repository.UserRepository;
import com.example.plantmanager.security.JwtUtil;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
@RequiredArgsConstructor
@Transactional
public class AuthService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Value("${spring.security.oauth2.client.registration.google.client-id:}")
    private String googleClientId;
    @Value("${spring.security.oauth2.client.registration.google.client-secret:}")
    private String googleClientSecret;
    @Value("${spring.security.oauth2.client.registration.google.redirect-uri:}")
    private String googleRedirectUri;

    private GoogleIdTokenVerifier buildVerifier() {
        return new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), JacksonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(googleClientId))
                .build();
    }

    public AuthResponse register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email already in use");
        }
        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
        AppUser u = AppUser.builder()
                .username(req.getUsername())
                .email(req.getEmail())
                .password(passwordEncoder.encode(req.getPassword()))
                .provider(AuthProvider.LOCAL)
                .roles(new HashSet<>(List.of(roleUser)))
                .build();
        userRepository.save(u);
        User springUser = new User(u.getEmail(), u.getPassword(), List.of(() -> roleUser.getName()));
        String token = jwtUtil.generateToken(springUser, Map.of("username", u.getUsername()));
        return AuthResponse.builder().token(token).user(toUserDto(u)).build();
    }

    public AuthResponse login(LoginRequest req) {
        Authentication auth = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword()));
        AppUser u = userRepository.findByEmail(req.getEmail()).orElseThrow();
        String token = jwtUtil.generateToken((User) auth.getPrincipal(), Map.of("username", u.getUsername()));
        return AuthResponse.builder().token(token).user(toUserDto(u)).build();
    }

    public AuthResponse loginWithGoogleCredential(GoogleAuthRequest req) {
        try {
            GoogleIdTokenVerifier verifier = buildVerifier();
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), req.getCredential());
            if (!verifier.verify(idToken)) {
                throw new BadCredentialsException("Invalid Google ID token");
            }
            GoogleIdToken.Payload p = idToken.getPayload();
            String email = p.getEmail();
            String name = (String) p.get("name");
            return provisionGoogleUser(email, name);
        } catch (Exception e) {
            throw new BadCredentialsException("Google authentication failed");
        }
    }

    public AuthResponse loginWithAuthorizationCode(String code) {
        try {
            Map<String, String> form = new LinkedHashMap<>();
            form.put("code", code);
            form.put("client_id", googleClientId);
            form.put("client_secret", googleClientSecret);
            form.put("redirect_uri", googleRedirectUri);
            form.put("grant_type", "authorization_code");
            String body = toFormUrlEncoded(form);

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("https://oauth2.googleapis.com/token"))
                    .header("Content-Type", "application/x-www-form-urlencoded")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();
            HttpResponse<String> response = HttpClient.newHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() >= 300) throw new BadCredentialsException("Failed token exchange");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(response.body());
            String idTokenStr = node.path("id_token").asText();
            if (idTokenStr == null || idTokenStr.isEmpty()) throw new BadCredentialsException("No id_token returned");

            GoogleIdTokenVerifier verifier = buildVerifier();
            GoogleIdToken idToken = GoogleIdToken.parse(JacksonFactory.getDefaultInstance(), idTokenStr);
            if (!verifier.verify(idToken)) throw new BadCredentialsException("Invalid id_token");
            GoogleIdToken.Payload p = idToken.getPayload();
            String email = p.getEmail();
            String name = (String) p.get("name");
            return provisionGoogleUser(email, name);
        } catch (Exception e) {
            throw new BadCredentialsException("Google code exchange failed");
        }
    }

    private String toFormUrlEncoded(Map<String, String> form) {
        StringBuilder sb = new StringBuilder();
        form.forEach((k, v) -> {
            if (sb.length() > 0) sb.append('&');
            sb.append(k).append('=').append(java.net.URLEncoder.encode(v, java.nio.charset.StandardCharsets.UTF_8));
        });
        return sb.toString();
    }

    private AuthResponse provisionGoogleUser(String email, String name) {
        Role roleUser = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> roleRepository.save(Role.builder().name("ROLE_USER").build()));
        AppUser u = userRepository.findByEmail(email).orElseGet(() -> {
            AppUser nu = AppUser.builder()
                    .username(name != null ? name : email)
                    .email(email)
                    .provider(AuthProvider.GOOGLE)
                    .roles(new HashSet<>(List.of(roleUser)))
                    .build();
            return userRepository.save(nu);
        });
        User springUser = new User(u.getEmail(), u.getPassword() == null ? "" : u.getPassword(),
                u.getRoles().stream().map(r -> (org.springframework.security.core.GrantedAuthority) r::getName).toList());
        String token = jwtUtil.generateToken(springUser, Map.of("username", u.getUsername()));
        return AuthResponse.builder().token(token).user(toUserDto(u)).build();
    }

    public UserDto me(String email) {
        AppUser u = userRepository.findByEmail(email).orElseThrow();
        return toUserDto(u);
    }

    private UserDto toUserDto(AppUser u) {
        return UserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .email(u.getEmail())
                .provider(u.getProvider())
                .roles(u.getRoles().stream().map(Role::getName).collect(java.util.stream.Collectors.toSet()))
                .build();
    }
}
