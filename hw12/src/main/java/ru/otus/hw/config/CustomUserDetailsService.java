package ru.otus.hw.config;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static ru.otus.hw.config.AppAuthorities.ADMIN;
import static ru.otus.hw.config.AppAuthorities.USER;

public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users;

    public CustomUserDetailsService(PasswordEncoder encoder) {
        users = Map.of(
                "admin", new CustomUserDetails("admin", encoder.encode("admin"), List.of(ADMIN)),
                "user", new CustomUserDetails("user", encoder.encode("user"), List.of(USER))
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = users.get(username);

        if (Objects.isNull(user)) {
            throw new UsernameNotFoundException("User not found");
        }

        return user;
    }

}
