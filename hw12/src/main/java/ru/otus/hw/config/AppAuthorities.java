package ru.otus.hw.config;

import org.springframework.security.core.GrantedAuthority;

public enum AppAuthorities implements GrantedAuthority {

    ADMIN("ROLE_ADMIN"),
    USER("ROLE_USER");

    private final String role;

    AppAuthorities(String role) {
        this.role = role;
    }

    @Override
    public String getAuthority() {
        return this.role;
    }

}
