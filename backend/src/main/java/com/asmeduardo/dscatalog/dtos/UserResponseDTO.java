package com.asmeduardo.dscatalog.dtos;

import com.asmeduardo.dscatalog.entities.User;

import java.util.List;

public record UserResponseDTO(
        Long id,
        String firstName,
        String lastName,
        String email,
        List<RoleDTO> roles
) {
    public UserResponseDTO(User entity) {
        this(
                entity.getId(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getEmail(),
                entity.getRoles().stream().map(RoleDTO::new).toList());
    }
}
