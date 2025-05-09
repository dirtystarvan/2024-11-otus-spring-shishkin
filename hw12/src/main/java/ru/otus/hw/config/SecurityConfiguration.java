package ru.otus.hw.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import ru.otus.hw.repositories.UserDetailsRepository;

/*
    login: admin - password: admin
    login: user - password: user
 */

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement((session) -> session
                    .sessionCreationPolicy(SessionCreationPolicy.ALWAYS))
            .authorizeHttpRequests((authorize) -> authorize
                    .requestMatchers("/createBook", "/").authenticated()
                    .requestMatchers("/editBook", "/deleteBook").authenticated()
            )
            .httpBasic(Customizer.withDefaults())
            .formLogin(AbstractAuthenticationFilterConfigurer::permitAll);

        return http.build();
    }

    @Bean
    public CustomUserDetailsService customUserDetailsService(UserDetailsRepository repository) {
        return new CustomUserDetailsService(repository);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
