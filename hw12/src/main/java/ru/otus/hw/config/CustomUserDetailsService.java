package ru.otus.hw.config;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import ru.otus.hw.repositories.UserDetailsRepository;

import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserDetailsRepository userDetailsRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var user = userDetailsRepository.findByName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException("User not found");
        }

        var actualUser = user.get();

        return new CustomUserDetails(actualUser.getName(), actualUser.getPassword(), List.of(actualUser.getAuthority()));
    }

}
