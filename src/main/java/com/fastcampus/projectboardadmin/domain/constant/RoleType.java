package com.fastcampus.projectboardadmin.domain.constant;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum RoleType {
    User("ROLE_USER"),

    MANAGER("ROLE_MANAGER"),

    DEVELOPER("ROLE_DEVELOPER"),

    ADMIN("ROLE_ADMIN");


    @Getter
    private final String roleName;

}