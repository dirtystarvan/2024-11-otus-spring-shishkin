package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.jpa.JpaBook;

import java.util.List;
import java.util.Optional;

public interface JpaBookRepository extends JpaRepository<JpaBook, Long> {
    @EntityGraph(value = "book-authors-genres")
    Optional<JpaBook> findById(long id);

    @EntityGraph(value = "book-authors-genres")
    List<JpaBook> findAll();
}
