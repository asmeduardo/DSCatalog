package com.asmeduardo.dscatalog.dtos;

import java.util.List;

public record UserRequestDTO(
        String firstName,
        String lastName,
        String email,
        String password,
        List<RoleDTO> roles
) {}

