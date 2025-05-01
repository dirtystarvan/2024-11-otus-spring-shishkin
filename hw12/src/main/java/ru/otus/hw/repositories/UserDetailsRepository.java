package ru.otus.hw.repositories;


import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.auth.User;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<User, Long> {

    @EntityGraph("users-authorities")
    Optional<User> findByName(String username);

}
