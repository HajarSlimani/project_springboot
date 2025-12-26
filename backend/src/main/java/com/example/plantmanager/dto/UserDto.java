package com.example.plantmanager.dto;

import com.example.plantmanager.model.AuthProvider;
import lombok.*;

import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDto {
    private Long id;
    private String username;
    private String email;
    private AuthProvider provider;
    private Set<String> roles;
}
