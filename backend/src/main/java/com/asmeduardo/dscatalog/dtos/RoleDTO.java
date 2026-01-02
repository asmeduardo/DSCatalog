package com.asmeduardo.dscatalog.dtos;

import com.asmeduardo.dscatalog.entities.Role;

public record RoleDTO(Long id, String authority) {

    public RoleDTO(Role entity) {
        this(entity.getId(), entity.getAuthority());
    }
}
